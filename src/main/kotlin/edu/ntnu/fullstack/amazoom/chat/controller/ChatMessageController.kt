package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.dto.ConversationSummaryDto
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
    ): ResponseEntity<Page<ConversationSummaryDto>> {
        val pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(chatMessageService.getUniqueConversations(pageable))
    }

    @GetMapping("/conversation/{listingId}/{otherUserId}")
    fun getConversation(
        @PathVariable listingId: Long,
        @PathVariable otherUserId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ChatMessageDto>> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"))
        return ResponseEntity.ok(
            chatMessageService.getMessagesForConversation(
                otherUserId,
                listingId,
                pageable
            )
        )
    }

    @PostMapping("/read/{listingId}/{otherUserId}")
    fun markMessagesAsRead(
        @PathVariable listingId: Long,
        @PathVariable otherUserId: Long,
    ): ResponseEntity<Unit> {
        chatMessageService.markMessagesAsRead(otherUserId, listingId)
        return ResponseEntity.noContent().build()
    }
}