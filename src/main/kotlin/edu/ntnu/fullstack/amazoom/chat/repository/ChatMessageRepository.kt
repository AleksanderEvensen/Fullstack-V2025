package edu.ntnu.fullstack.amazoom.chat.repository

import edu.ntnu.fullstack.amazoom.chat.entity.ChatMessage
import edu.ntnu.fullstack.amazoom.common.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    @Query("""
        SELECT m FROM ChatMessage m 
        WHERE (m.sender.id= :userIdA AND m.recipient.id = :userIdB) 
           OR (m.sender.id = :userIdB AND m.recipient.id = :userIdA) 
        ORDER BY m.timestamp DESC
    """)
    fun findMessagesBetweenUsers(
        @Param("userIdA") userIdA: UUID,
        @Param("userIdB") userIdB: UUID,
        pageable: Pageable
    ): Page<ChatMessage>

    @Modifying
    @Query("UPDATE ChatMessage m SET m.read = true WHERE m.recipient.id = :id AND m.sender.id = :otherId AND m.read = false")
    fun markMessagesAsRead(
        @Param("id") id: UUID,
        @Param("otherId") otherId: UUID
    ): Int

    @Query("""
        SELECT DISTINCT 
            CASE 
                WHEN m.sender.id = :userId THEN m.recipient
                ELSE m.sender
            END
        FROM ChatMessage m
        WHERE m.sender.id = :userId OR m.recipient.id = :userId
        ORDER BY MAX(m.timestamp) DESC
    """)
    fun findDistinctChatPartners(@Param("userId") userId: UUID, pageable: Pageable): Page<User>

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.recipient.id = :userId AND m.sender.id = :otherUserId AND m.read = false")
    fun countUnreadMessagesFromUser(
        @Param("userId") userId: UUID,
        @Param("otherUserId") otherUserId: UUID
    ): Long

    @Query("""
        SELECT m FROM ChatMessage m
        WHERE (m.sender.id = :userIdA AND m.recipient.id = :userIdB) 
           OR (m.sender.id = :userIdB AND m.recipient.id = :userIdA)
        ORDER BY m.timestamp DESC
    """)
    fun findLatestMessageBetweenUsers(
        @Param("userIdA") userIdA: UUID,
        @Param("userIdB") userIdB: UUID,
        pageable: Pageable
    ): Page<ChatMessage>
}