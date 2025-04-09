package edu.ntnu.fullstack.amazoom.chat.mapper

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.entity.ChatMessage
import edu.ntnu.fullstack.amazoom.common.mapper.UserMapper

object ChatMessageMapper {
    fun toDto(chatMessage: ChatMessage): ChatMessageDto {
        return ChatMessageDto(
            sender = UserMapper.toDto(chatMessage.sender),
            recipient = UserMapper.toDto(chatMessage.recipient),
            listingId = chatMessage.listing.id,
            content = chatMessage.content,
            timestamp = chatMessage.timestamp,
            read = chatMessage.read
        )
    }
}