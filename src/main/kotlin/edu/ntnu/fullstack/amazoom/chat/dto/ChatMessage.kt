package edu.ntnu.fullstack.amazoom.chat.dto

import edu.ntnu.fullstack.amazoom.common.dto.UserDto
import java.time.Instant

/**
 * Data Transfer Object (DTO) for a chat message.
 * Contains message content and metadata.
 */
data class ChatMessageDto(
    val sender: UserDto,
    val recipient: UserDto,
    val listingId: Long,
    val content: String,
    val timestamp: Instant,
)

/**
 * Data Transfer Object (DTO) for the last message in a conversation.
 * Provides a compact summary of the message.
 */
data class LastMessageDto (
    val content: String,
    val timestamp: Instant,
)

/**
 * Data Transfer Object (DTO) for a message request.
 * Contains data needed to send a new message.
 */
data class ChatMessageRequestDto(
    val listingId: Long,
    val recipientId: Long,
    val content: String
)