package edu.ntnu.fullstack.amazoom.chat.dto

import edu.ntnu.fullstack.amazoom.common.dto.UserDto
import java.time.Instant
import java.util.UUID

data class ChatMessageDto(
    val sender: UserDto,
    val recipient: UserDto,
    val content: String,
    val timestamp: Instant,
    val read: Boolean
)

data class LastMessageDto (
    val content: String,
    val timestamp: Instant,
    val isFromCurrentUser: Boolean
)

data class ReadReceiptDto(
    val senderId: Long,
    val recipientId: Long,
    val timestamp: Instant,
    val listingId: Long
)

data class ChatMessageRequestDto(
    val listingId: Long,
    val recipientId: Long,
    val content: String
)