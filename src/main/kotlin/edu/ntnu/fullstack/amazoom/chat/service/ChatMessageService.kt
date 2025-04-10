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
import org.springframework.stereotype.Service
import org.springframework.web.util.HtmlUtils
import java.time.Instant

/**
 * Service class for handling chat messages.
 * Provides methods for sending messages, retrieving conversations, and long polling for new messages.
 */
@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
    private val userService: UserService,
    private val listingService: ListingService,
    private val listingRepository: ListingRepository
) {
    private val logger = LoggerFactory.getLogger(ChatMessageService::class.java)
    private val MAX_MESSAGE_LENGTH = 1000
    private val LONG_POLL_TIMEOUT = 30000L
    private val POLLING_SLEEP_INTERVAL = 500L

    /**
     * Gets unique conversations for the current user.
     */
    fun getUniqueConversations(): List<ConversationSummaryDto> {
        val currentUser = userService.getCurrentUser()
        val currentUserId = currentUser.id

        logger.debug("Getting unique conversations for user: {}", currentUserId)

        val uniqueConversations = chatMessageRepository.findUniqueConversationIds(currentUserId)

        return uniqueConversations.map { conversation ->
            val otherUserId = conversation.otherUserId
            val listingId = conversation.listingId

            val otherUser = userService.getUserById(otherUserId)
            val listing = listingService.getListing(listingId)

            val latestMessage = chatMessageRepository.findMessagesBetweenUsersForListing(
                currentUserId, otherUserId, listingId
            ).firstOrNull()

            val lastMessageDto = latestMessage?.let {
                LastMessageDto(
                    content = it.content,
                    timestamp = it.timestamp,
                )
            }

            ConversationSummaryDto(
                user = UserMapper.toDto(otherUser),
                listingId = listingId,
                listingTitle = listing.title,
                lastMessage = lastMessageDto
            )
        }
    }

    /**
     * Gets messages for a specific conversation.
     */
    fun getMessagesForConversation(
        otherUserId: Long,
        listingId: Long
    ): List<ChatMessageDto> {
        val userId = userService.getCurrentUser().id

        logger.debug("Getting messages between users {} and {} for listing: {}",
            userId, otherUserId, listingId)

        val messages = chatMessageRepository.findMessagesBetweenUsersForListing(
            userId,
            otherUserId,
            listingId
        )

        return messages.map { ChatMessageMapper.toDto(it) }
    }

    /**
     * Sends a new message.
     */
    @Transactional
    fun sendMessage(request: ChatMessageRequestDto): ChatMessageDto {
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

        logger.info("Message sent from user {} to user {} about listing: {}",
            sender.id, recipient.id, listing.id)

        return ChatMessageMapper.toDto(savedMessage)
    }

    /**
     * Long polls for new messages in a specific conversation.
     */
    @Throws(InterruptedException::class)
    fun pollForMessages(
        lastMessageTimestamp: Instant,
        otherUserId: Long,
        listingId: Long
    ): List<ChatMessageDto> {
        val currentUser = userService.getCurrentUser()
        val startTime = System.currentTimeMillis()

        while (System.currentTimeMillis() - startTime < LONG_POLL_TIMEOUT) {
            // Check for new messages in this specific conversation
            val newMessages = chatMessageRepository.findNewMessagesForConversation(
                currentUserId = currentUser.id,
                otherUserId = otherUserId,
                listingId = listingId,
                since = lastMessageTimestamp,
            )

            if (newMessages.isNotEmpty()) {
                logger.debug("Found {} new messages for conversation", newMessages.size)
                return newMessages.map { ChatMessageMapper.toDto(it) }
            }

            // Sleep for a short interval before checking again
            Thread.sleep(POLLING_SLEEP_INTERVAL)
        }

        // Return empty list after timeout
        return emptyList()
    }

    /**
     * Gets the current user.
     */
    fun getCurrentUser() = userService.getCurrentUser()

    /**
     * Verifies that a conversation between users about a listing is valid.
     * At least one of the users must be the seller of the listing.
     */
    private fun verifyConversationAccess(senderId: Long, recipientId: Long, listingId: Long) {
        val listing = listingService.getListing(listingId)

        if (listing.seller.id != senderId && listing.seller.id != recipientId) {
            logger.warn("Invalid conversation: neither participant is the listing owner")
            throw MessageAccessDeniedException()
        }
    }
}