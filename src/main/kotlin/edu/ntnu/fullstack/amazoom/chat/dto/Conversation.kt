package edu.ntnu.fullstack.amazoom.chat.dto

import edu.ntnu.fullstack.amazoom.common.dto.UserDto

/**
 * Data Transfer Object (DTO) for a pair of conversation identifiers.
 * Used internally for database queries.
 */
data class ConversationIdPairDto(
    val otherUserId: Long,
    val listingId: Long
)

/**
 * Data Transfer Object (DTO) for a conversation summary.
 * Contains information about a conversation between users.
 */
data class ConversationSummaryDto(
    val user: UserDto,
    val listingId: Long,
    val listingTitle: String,
    val unreadCount: Long,
    val lastMessage: LastMessageDto?
)