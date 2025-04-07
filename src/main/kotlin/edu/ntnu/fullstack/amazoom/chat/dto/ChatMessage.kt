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

data class ChatPartnerDto(
    val user: UserDto,
    val unreadCount: Long,
    val lastMessage: LastMessageDto
)

data class LastMessageDto (
    val content: String,
    val timestamp: Instant,
    val isFromCurrentUser: Boolean
)

data class ReadReceiptDto(
    val senderId: UUID,
    val recipientId: UUID,
    val timestamp: Instant
)

data class ChatMessageRequestDto(
    val recipientId: UUID,
    val content: String
)