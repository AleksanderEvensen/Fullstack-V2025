package edu.ntnu.fullstack.amazoom.chat.repository

import edu.ntnu.fullstack.amazoom.chat.dto.ConversationIdPairDto
import edu.ntnu.fullstack.amazoom.chat.entity.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    @Query(
        """
        SELECT m FROM ChatMessage m 
        WHERE ((m.sender.id= :userIdA AND m.recipient.id = :userIdB) 
           OR (m.sender.id = :userIdB AND m.recipient.id = :userIdA))
           AND m.listing.id = :listingId
        ORDER BY m.timestamp DESC
    """
    )
    fun findMessagesBetweenUsersForListing(
        @Param("userIdA") userIdA: Long,
        @Param("userIdB") userIdB: Long,
        @Param("listingId") listingId: Long
    ): List<ChatMessage>

    @Query("""
        SELECT new edu.ntnu.fullstack.amazoom.chat.dto.ConversationIdPairDto(
            CASE
                WHEN m.sender.id = :userId THEN m.recipient.id
                ELSE m.sender.id
            END,
            m.listing.id
        )
        FROM ChatMessage m
        WHERE m.sender.id = :userId OR m.recipient.id = :userId
        GROUP BY
            CASE
                WHEN m.sender.id = :userId THEN m.recipient.id
                ELSE m.sender.id
            END,
            m.listing.id
        ORDER BY MAX(m.timestamp) DESC
     """)
    fun findUniqueConversationIds(
        @Param("userId") userId: Long
    ): List<ConversationIdPairDto>

    @Query("""
        SELECT m FROM ChatMessage m
        JOIN FETCH m.sender
        JOIN FETCH m.recipient
        WHERE ((m.sender.id = :otherUserId AND m.recipient.id = :currentUserId)
               OR (m.sender.id = :currentUserId AND m.recipient.id = :otherUserId))
          AND m.listing.id = :listingId
          AND m.timestamp > :since
        ORDER BY m.timestamp ASC
    """)
    fun findNewMessagesForConversation(
        @Param("currentUserId") currentUserId: Long,
        @Param("otherUserId") otherUserId: Long,
        @Param("listingId") listingId: Long,
        @Param("since") since: Instant
    ): List<ChatMessage>
}