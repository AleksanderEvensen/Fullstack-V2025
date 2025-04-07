package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDTO
import edu.ntnu.fullstack.amazoom.chat.dto.ChatPartnerDTO
import edu.ntnu.fullstack.amazoom.chat.service.ChatMessageService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/chat")
class ChatMessageController(
    private val chatMessageService: ChatMessageService,
) {
    @GetMapping("/conversations")
    fun getConversations(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ChatPartnerDTO>> {
        val pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(chatMessageService.getChatPartners(pageable))
    }

    @GetMapping("/conversation/{otherUserId}")
    fun getConversation(
        @PathVariable otherUserId: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ChatMessageDTO>> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"))
        return ResponseEntity.ok(
            chatMessageService.getMessagesBetweenUsers(
                otherUserId,
                pageable
            )
        )
    }

    @PostMapping("/read/{otherUserId}")
    fun markMessagesAsRead(
        @PathVariable otherUserId: UUID,
    ): ResponseEntity<Unit> {
        chatMessageService.markMessagesAsRead(otherUserId)
        return ResponseEntity.noContent().build()
    }
}