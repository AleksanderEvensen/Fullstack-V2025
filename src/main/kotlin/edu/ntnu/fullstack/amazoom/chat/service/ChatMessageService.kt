package edu.ntnu.fullstack.amazoom.chat.service

import edu.ntnu.fullstack.amazoom.chat.dto.*
import edu.ntnu.fullstack.amazoom.chat.entity.ChatMessage
import edu.ntnu.fullstack.amazoom.chat.repository.ChatMessageRepository
import edu.ntnu.fullstack.amazoom.common.service.UserService
import edu.ntnu.fullstack.amazoom.listing.service.ListingService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.web.util.HtmlUtils
import java.time.Instant
import java.util.UUID

@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val userService: UserService,
    private val listingService: ListingService
) {
    fun getUniqueConversations(pageable: Pageable): Page<ConversationSummaryDto> {
        val currentUser = userService.getCurrentUser()
        val currentUserId = currentUser.id

        val uniqueConversations = chatMessageRepository.findUniqueConversationIds(currentUserId, pageable)

        return uniqueConversations.map { conversation ->
            val otherUserId = conversation.otherUserId
            val listingId = conversation.listingId

            val otherUser = userService.getUserById(otherUserId)
            val listing = listingService.getListing(listingId)
            val unreadCount = chatMessageRepository.countUnreadMessagesForConversation(currentUserId, otherUserId, listingId)

            val latestMessagePage = PageRequest.of(0, 1)
            val latestMessage = chatMessageRepository.findMessagesBetweenUsersForListing(currentUserId, otherUserId, listingId, latestMessagePage).firstOrNull()

            val lastMessageDto = latestMessage?.let {
                LastMessageDto(
                    content = it.content,
                    timestamp = it.timestamp,
                    isFromCurrentUser = it.sender.id == currentUserId
                )
            }


            // Build the conversation summary
            ConversationSummaryDto(
                user = otherUser.toDto(),
                listingId = listingId,
                listingTitle = listing.title,
                unreadCount = unreadCount,
                lastMessage = lastMessageDto
            )
        }
    }

    @Transactional
    fun getMessagesForConversation(
        otherUserId: UUID,
        listingId: Long,
        pageable: Pageable,
    ): Page<ChatMessageDto> {
        val userId = userService.getCurrentUser().id

        val messages = chatMessageRepository.findMessagesBetweenUsersForListing(
            userId,
            otherUserId,
            listingId,
            pageable
        )

        markMessagesAsRead(otherUserId, listingId)

        return messages.map { it.toDto() }
    }

    @Transactional
    fun markMessagesAsRead(
        otherUserId: UUID,
        listingId: Long
    ) {
        val userId = userService.getCurrentUser().id

        val count = chatMessageRepository.markMessagesAsRead(userId, otherUserId, listingId)

        if (count > 0) {
            val readReceipt = ReadReceiptDto(
                senderId = userId,
                recipientId = otherUserId,
                timestamp = Instant.now(),
                listingId = listingId
            )

            simpMessagingTemplate.convertAndSendToUser(
                otherUserId.toString(),
                "/queue/receipts",
                readReceipt
            )
        }
    }

    @Transactional
    fun sendMessage(request: ChatMessageRequestDto): ChatMessageDto {
        val sender = userService.getCurrentUser()
        val recipient = userService.getUserById(request.recipientId)

        val listing = listingService.getListing(request.listingId)

        verifyConversationAccess(sender.id, recipient.id, listing.id)

        val sanitizedContent = HtmlUtils.htmlEscape(request.content)

        val chatMessage = ChatMessage(
            sender = sender,
            recipient = recipient,
            listing = listing,
            content = sanitizedContent,
            timestamp = Instant.now(),
            read = false
        )

        val savedMessage = chatMessageRepository.save(chatMessage)
        val messageDto = savedMessage.toDto()

        simpMessagingTemplate.convertAndSendToUser(
            request.recipientId.toString(),
            "/queue/messages",
            messageDto
        )

        return messageDto
    }

    private fun verifyConversationAccess(senderId: UUID, recipientId: UUID, listingId: Long) {
        val listing = listingService.getListing(listingId)

        if (listing.seller.id != senderId && listing.seller.id != recipientId) {
            throw IllegalArgumentException("Invalid conversation: neither participant is the listing owner")
        }
    }
}