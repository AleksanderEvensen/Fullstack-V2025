package edu.ntnu.fullstack.amazoom.chat.repository

import edu.ntnu.fullstack.amazoom.chat.dto.LastMessageDto
import edu.ntnu.fullstack.amazoom.chat.entity.ChatMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

/**
 * Repository interface for managing chat messages.
 * Provides methods for querying messages between users and retrieving conversations.
 */
@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {

    /**
     * Finds messages between two users for a specific listing with pagination.
     */
    @Query(
        """
        SELECT m FROM ChatMessage m 
        WHERE ((m.sender.id = :userIdA AND m.recipient.id = :userIdB) 
           OR (m.sender.id = :userIdB AND m.recipient.id = :userIdA))
           AND m.listing.id = :listingId
        ORDER BY m.timestamp DESC
    """
    )
    fun findMessagesBetweenUsersForListing(
        @Param("userIdA") userIdA: Long,
        @Param("userIdB") userIdB: Long,
        @Param("listingId") listingId: Long,
        pageable: Pageable
    ): Page<ChatMessage>

    /**
     * Finds all conversations for a user with pagination using JPQL
     * This avoids potential compatibility issues with native SQL in different databases
     */
    @Query(
        """
    SELECT new edu.ntnu.fullstack.amazoom.chat.repository.ConversationProjection(
        CASE WHEN m.sender.id = :userId THEN m.recipient.id ELSE m.sender.id END,
        m.listing.id,
        m.content,
        m.timestamp
    )
    FROM ChatMessage m
    WHERE (m.sender.id = :userId OR m.recipient.id = :userId)
    AND m.timestamp = (
        SELECT MAX(msg.timestamp)
        FROM ChatMessage msg
        WHERE 
            ( (msg.sender.id = m.sender.id AND msg.recipient.id = m.recipient.id)
              OR
              (msg.sender.id = m.recipient.id AND msg.recipient.id = m.sender.id) )
            AND msg.listing.id = m.listing.id
    )
    ORDER BY m.timestamp DESC
"""
    )
    fun findConversationsByUserId(
        @Param("userId") userId: Long,
        pageable: Pageable
    ): Page<ConversationProjection>
}

/**
 * Projection class for conversation data
 */
class ConversationProjection(
    val otherUserId: Long,
    val listingId: Long,
    private val lastMessageContent: String?,
    private val lastMessageTimestamp: Instant?
) {
    val lastMessage: LastMessageDto?
        get() = if (lastMessageContent != null && lastMessageTimestamp != null) {
            LastMessageDto(lastMessageContent, lastMessageTimestamp)
        } else {
            null
        }
}