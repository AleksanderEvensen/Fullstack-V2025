package edu.ntnu.fullstack.amazoom.chat.entity

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "chat_messages")
data class ChatMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    val sender: User,

    @ManyToOne(fetch = FetchType.LAZY)
    val recipient: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    val listing: Listing,

    @Column(nullable = false, length = 2000)
    val content: String,

    val timestamp: Instant = Instant.now(),

    var read: Boolean = false
) {
    fun toDto(): ChatMessageDto {
        return ChatMessageDto(
            sender = sender.toDto(),
            recipient = recipient.toDto(),
            content = content,
            timestamp = timestamp,
            read = read
        )
    }
}