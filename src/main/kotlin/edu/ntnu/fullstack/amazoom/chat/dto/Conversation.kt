package edu.ntnu.fullstack.amazoom.chat.dto

import edu.ntnu.fullstack.amazoom.common.dto.UserDto
import java.util.UUID

data class ConversationIdPairDto(
    val otherUserId: Long,
    val listingId: Long
)

data class ConversationSummaryDto(
    val user: UserDto,
    val listingId: Long,
    val listingTitle: String,
    val unreadCount: Long,
    val lastMessage: LastMessageDto?
)