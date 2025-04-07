package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageRequestDto
import edu.ntnu.fullstack.amazoom.chat.service.ChatMessageService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
class ChatWebSocketController(
    private val chatMessageService: ChatMessageService,
) {

    @MessageMapping("/send")
    fun sendMessage(@Payload request: ChatMessageRequestDto): ChatMessageDto {
        return chatMessageService.sendMessage(request)
    }
}