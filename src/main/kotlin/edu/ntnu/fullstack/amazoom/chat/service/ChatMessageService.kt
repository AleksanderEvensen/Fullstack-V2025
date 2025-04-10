package edu.ntnu.fullstack.amazoom.chat.service

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageRequestDto
import edu.ntnu.fullstack.amazoom.chat.dto.ConversationSummaryDto
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
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.util.HtmlUtils
import java.time.Instant

/**
 * Service class for handling chat messages.
 * Provides methods for sending messages and retrieving conversations.
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

    /**
     * Gets conversations for the current user with pagination.
     *
     * @param pageable Pagination information
     * @return Page of conversation summaries
     */
    fun getConversations(pageable: Pageable): Page<ConversationSummaryDto> {
        val currentUser = userService.getCurrentUser()

        logger.debug("Getting paginated conversations for user: {}", currentUser.id)

        return chatMessageRepository.findConversationsByUserId(currentUser.id, pageable)
            .map { conv ->
                val otherUser = userService.getUserById(conv.otherUserId)
                val listing = listingService.getListing(conv.listingId)

                ConversationSummaryDto(
                    user = UserMapper.toDto(otherUser),
                    listingId = conv.listingId,
                    listingTitle = listing.title,
                    lastMessage = conv.lastMessage
                )
            }
    }

    /**
     * Gets messages for a specific conversation between current user, another user, and a listing,
     * with pagination.
     *
     * @param otherUserId ID of the other user in the conversation
     * @param listingId ID of the listing the conversation is about
     * @param pageable Pagination information
     * @return Page of chat messages
     */
    fun getMessagesForConversation(
        otherUserId: Long,
        listingId: Long,
        pageable: Pageable
    ): Page<ChatMessageDto> {
        val userId = userService.getCurrentUser().id

        logger.debug("Getting paginated messages between users {} and {} for listing: {}",
            userId, otherUserId, listingId)

        return chatMessageRepository.findMessagesBetweenUsersForListing(
            userId,
            otherUserId,
            listingId,
            pageable
        ).map { ChatMessageMapper.toDto(it) }
    }

    /**
     * Sends a new message from current user to other user.
     *
     * @param request The message request
     * @return The sent message
     * @throws InvalidMessageException if the message content is invalid
     * @throws MessageTooLongException if the message is too long
     * @throws RecipientNotFoundException if the recipient does not exist
     * @throws ListingNotFoundException if the listing does not exist
     * @throws MessageAccessDeniedException if the user is not allowed to send messages in this conversation
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

        val listing = listingRepository.findById(request.listingId).orElseThrow {
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
     * Verifies that a conversation between users about a listing is valid.
     * At least one of the users must be the seller of the listing.
     *
     * @param senderId ID of the message sender
     * @param recipientId ID of the message recipient
     * @param listingId ID of the listing
     * @throws MessageAccessDeniedException if the conversation is invalid
     */
    private fun verifyConversationAccess(senderId: Long, recipientId: Long, listingId: Long) {
        val listing = listingService.getListing(listingId)

        if (listing.seller.id != senderId && listing.seller.id != recipientId) {
            logger.warn("Invalid conversation: neither participant is the listing owner")
            throw MessageAccessDeniedException()
        }
    }
}