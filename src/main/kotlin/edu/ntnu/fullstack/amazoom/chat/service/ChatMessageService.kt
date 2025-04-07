package edu.ntnu.fullstack.amazoom.chat.service

import edu.ntnu.fullstack.amazoom.chat.dto.*
import edu.ntnu.fullstack.amazoom.chat.entity.ChatMessage
import edu.ntnu.fullstack.amazoom.chat.repository.ChatMessageRepository
import edu.ntnu.fullstack.amazoom.common.service.UserService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val userService: UserService
) {
    fun getChatPartners(pageable: Pageable): Page<ChatPartnerDto> {
        val currentUserId = userService.getCurrentUser().id

        return chatMessageRepository.findDistinctChatPartners(currentUserId, pageable).map { user ->
            val latestMessagePageable = PageRequest.of(0, 1)
            val latestMessage = chatMessageRepository.findLatestMessageBetweenUsers(
                currentUserId,
                user.id,
                latestMessagePageable
            ).firstOrNull()

            val unreadCount = chatMessageRepository.countUnreadMessagesFromUser(currentUserId, user.id)

            ChatPartnerDto(
                user = user.toDto(),
                unreadCount = unreadCount,
                lastMessage = latestMessage?.let { LastMessageDto(
                    content = latestMessage.content,
                    timestamp = latestMessage.timestamp,
                    isFromCurrentUser = latestMessage.sender.id == currentUserId
                ) } ?: LastMessageDto(
                    content = "",
                    timestamp = Instant.EPOCH,
                    isFromCurrentUser = false
                )
            )
        }
    }

    fun getMessagesBetweenUsers(
        otherUserId: UUID,
        pageable: Pageable,
    ): Page<ChatMessageDto> {
        val userId = userService.getCurrentUser().id

        val messages = chatMessageRepository.findMessagesBetweenUsers(userId, otherUserId, pageable)

        return messages.map {
            it.toDto()
        }
    }

    @Transactional
    fun markMessagesAsRead(
        otherUserId: UUID
    ) {
        val userId = userService.getCurrentUser().id

        val count = chatMessageRepository.markMessagesAsRead(userId, otherUserId)

        if (count > 0) {
            val readReceipt = ReadReceiptDto(
                senderId = userId,
                recipientId = otherUserId,
                timestamp = Instant.now()
            )

            simpMessagingTemplate.convertAndSendToUser(
                otherUserId.toString(),
                "/queue/receipts",
                readReceipt
            )
        }
    }

    @Transactional
    fun sendMessage(request: ChatMessageRequestDto): ChatMessageDto {
        val sender = userService.getCurrentUser()
        val recipient = userService.getUserById(request.recipientId)

        val chatMessage = ChatMessage(
            sender = sender,
            recipient = recipient,
            content = request.content,
            timestamp = Instant.now(),
            read = false
        )

        val savedMessage = chatMessageRepository.save(chatMessage)
        val messageDto = savedMessage.toDto()

        simpMessagingTemplate.convertAndSendToUser(
            request.recipientId.toString(),
            "/queue/messages",
            messageDto
        )

        return messageDto
    }
}