package edu.ntnu.fullstack.amazoom.chat.service

import edu.ntnu.fullstack.amazoom.chat.dto.*
import edu.ntnu.fullstack.amazoom.chat.entity.ChatMessage
import edu.ntnu.fullstack.amazoom.chat.exception.InvalidMessageException
import edu.ntnu.fullstack.amazoom.chat.exception.MessageAccessDeniedException
import edu.ntnu.fullstack.amazoom.chat.exception.MessageTooLongException
import edu.ntnu.fullstack.amazoom.chat.exception.RecipientNotFoundException
import edu.ntnu.fullstack.amazoom.chat.mapper.ChatMessageMapper
import edu.ntnu.fullstack.amazoom.chat.repository.ChatMessageRepository
import edu.ntnu.fullstack.amazoom.common.mapper.UserMapper
import edu.ntnu.fullstack.amazoom.common.service.UserService
import edu.ntnu.fullstack.amazoom.listing.exception.ListingNotFoundException
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import edu.ntnu.fullstack.amazoom.listing.service.ListingService
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.web.util.HtmlUtils
import java.time.Instant

/**
 * Service for managing chat messages.
 * Handles sending, retrieving, and marking messages as read.
 */
@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val userService: UserService,
    private val listingService: ListingService,
    private val listingRepository: ListingRepository
) {
    private val logger = LoggerFactory.getLogger(ChatMessageService::class.java)
    private val MAX_MESSAGE_LENGTH = 1000

    /**
     * Gets unique conversations for the current user.
     *
     * @param pageable Pagination parameters
     * @return A page of conversation summaries
     */
    fun getUniqueConversations(pageable: Pageable): Page<ConversationSummaryDto> {
        val currentUser = userService.getCurrentUser()
        val currentUserId = currentUser.id

        logger.debug("Getting unique conversations for user: {}", currentUserId)

        val uniqueConversations = chatMessageRepository.findUniqueConversationIds(currentUserId, pageable)

        return uniqueConversations.map { conversation ->
            val otherUserId = conversation.otherUserId
            val listingId = conversation.listingId

            val otherUser = userService.getUserById(otherUserId)
            val listing = listingService.getListing(listingId)

            val latestMessagePage = PageRequest.of(0, 1)
            val latestMessage = chatMessageRepository.findMessagesBetweenUsersForListing(
                currentUserId, otherUserId, listingId, latestMessagePage
            ).firstOrNull()

            val lastMessageDto = latestMessage?.let {
                LastMessageDto(
                    content = it.content,
                    timestamp = it.timestamp,
                )
            }

            // Build the conversation summary
            ConversationSummaryDto(
                user = UserMapper.toDto(otherUser),
                listingId = listingId,
                listingTitle = listing.title,
                lastMessage = lastMessageDto
            )
        }
    }

    /**
     * Gets messages for a specific conversation between the current user and another user about a listing.
     *
     * @param otherUserId The ID of the other user in the conversation
     * @param listingId The ID of the listing being discussed
     * @param pageable Pagination parameters
     * @return A page of chat messages
     */
    fun getMessagesForConversation(
        otherUserId: Long,
        listingId: Long,
        pageable: Pageable,
    ): Page<ChatMessageDto> {
        val userId = userService.getCurrentUser().id

        logger.debug("Getting messages between users {} and {} for listing: {}",
            userId, otherUserId, listingId)

        val messages = chatMessageRepository.findMessagesBetweenUsersForListing(
            userId,
            otherUserId,
            listingId,
            pageable
        )

        return messages.map { ChatMessageMapper.toDto(it) }
    }

    /**
     * Sends a new message.
     *
     * @param request The message request data
     * @return The created message as a DTO
     * @throws InvalidMessageException if the message is invalid
     * @throws MessageTooLongException if the message is too long
     * @throws RecipientNotFoundException if the recipient doesn't exist
     */
    @Transactional
    fun sendMessage(request: ChatMessageRequestDto): ChatMessageDto {
        logger.debug("Sending message to recipient: {} for listing: {}",
            request.recipientId, request.listingId)

        if (request.content.isBlank()) {
            throw InvalidMessageException("Message content cannot be empty")
        }

        if (request.content.length > MAX_MESSAGE_LENGTH) {
            throw MessageTooLongException()
        }

        val sender = userService.getCurrentUser()
        val recipient = try {
            userService.getUserById(request.recipientId)
        } catch (e: Exception) {
            logger.error("Recipient not found with ID: {}", request.recipientId)
            throw RecipientNotFoundException(request.recipientId)
        }

        val listing = listingRepository.findById(request.listingId).orElseThrow{
            ListingNotFoundException()
        }

        verifyConversationAccess(sender.id, recipient.id, listing.id)

        val sanitizedContent = HtmlUtils.htmlEscape(request.content)

        val chatMessage = ChatMessage(
            sender = sender,
            recipient = recipient,
            listing = listing,
            content = sanitizedContent,
            timestamp = Instant.now(),
        )

        val savedMessage = chatMessageRepository.save(chatMessage)
        val messageDto = ChatMessageMapper.toDto(savedMessage)

        logger.info("Message sent from user {} to user {} about listing: {}",
            sender.id, recipient.id, listing.id)

        simpMessagingTemplate.convertAndSendToUser(
            request.recipientId.toString(),
            "/queue/messages",
            messageDto
        )

        return messageDto
    }

    /**
     * Verifies that a conversation between users about a listing is valid.
     * At least one of the users must be the seller of the listing.
     *
     * @param senderId The ID of the message sender
     * @param recipientId The ID of the message recipient
     * @param listingId The ID of the listing
     * @throws MessageAccessDeniedException if the conversation is invalid
     */
    private fun verifyConversationAccess(senderId: Long, recipientId: Long, listingId: Long) {
        val listing = listingService.getListing(listingId)

        if (listing.seller.id != senderId && listing.seller.id != recipientId) {
            logger.warn("Invalid conversation: neither participant is the listing owner. " +
                    "Sender: {}, Recipient: {}, Listing: {}, Owner: {}",
                senderId, recipientId, listingId, listing.seller.id)

            throw MessageAccessDeniedException()
        }
    }
}