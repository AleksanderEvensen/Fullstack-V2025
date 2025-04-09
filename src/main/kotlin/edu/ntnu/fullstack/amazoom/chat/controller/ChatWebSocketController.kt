package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageRequestDto
import edu.ntnu.fullstack.amazoom.chat.service.ChatMessageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

/**
 * WebSocket controller for chat messages.
 * Handles real-time message sending via WebSocket.
 */
@Controller
@Tag(name = "Chat WebSocket", description = "WebSocket operations for real-time chat messaging")
class ChatWebSocketController(
    private val chatMessageService: ChatMessageService,
) {
    /**
     * Handles sending a message via WebSocket.
     *
     * @param request The message request data
     * @return The created message
     */
    @Operation(
        summary = "Send message",
        description = "Sends a new chat message via WebSocket"
    )
    @MessageMapping("/send")
    fun sendMessage(@Payload @Valid request: ChatMessageRequestDto): ChatMessageDto {

        return chatMessageService.sendMessage(request)
    }
}