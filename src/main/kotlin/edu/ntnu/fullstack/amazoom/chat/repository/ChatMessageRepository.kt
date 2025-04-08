package edu.ntnu.fullstack.amazoom.chat.repository

import edu.ntnu.fullstack.amazoom.chat.dto.ConversationIdPairDto
import edu.ntnu.fullstack.amazoom.chat.entity.ChatMessage
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    @Query("""
        SELECT m FROM ChatMessage m 
        WHERE ((m.sender.id= :userIdA AND m.recipient.id = :userIdB) 
           OR (m.sender.id = :userIdB AND m.recipient.id = :userIdA))
           AND m.listing.id = :listingId
        ORDER BY m.timestamp DESC
    """)
    fun findMessagesBetweenUsersForListing(
        @Param("userIdA") userIdA: Long,
        @Param("userIdB") userIdB: Long,
        @Param("listingId") listingId: Long,
        pageable: Pageable
    ): Page<ChatMessage>

    @Modifying
    @Query("UPDATE ChatMessage m SET m.read = true WHERE m.recipient.id = :id AND m.sender.id = :otherId AND m.listing.id = :listingId AND m.read = false")
    fun markMessagesAsRead(
        @Param("id") id: Long,
        @Param("otherId") otherId: Long,
        @Param("listingId") listingId: Long
    ): Int

    @Query("""
        SELECT DISTINCT 
            CASE 
                WHEN m.sender.id = :userId THEN m.recipient.id 
                ELSE m.sender.id 
            END as otherUserId,
            m.listing.id as listingId
        FROM ChatMessage m
        WHERE m.sender.id = :userId OR m.recipient.id = :userId
        ORDER BY MAX(m.timestamp) DESC
    """)
    fun findUniqueConversationIds(
        @Param("userId") userId: Long,
        pageable: Pageable
    ): Page<ConversationIdPairDto>

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.recipient.id = :userId AND m.sender.id = :otherUserId AND m.listing.id = :listingId AND m.read = false")
    fun countUnreadMessagesForConversation(
        @Param("userId") userId: Long,
        @Param("otherUserId") otherUserId: Long,
        @Param("listingId") listingId: Long
    ): Long

}