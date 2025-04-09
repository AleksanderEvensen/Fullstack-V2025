package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.dto.ConversationSummaryDto
import edu.ntnu.fullstack.amazoom.chat.service.ChatMessageService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing chat messages.
 * Provides endpoints for retrieving conversations and messages.
 */
@RestController
@RequestMapping("/api/chat")
class ChatMessageController(
    private val chatMessageService: ChatMessageService,
) {
    /**
     * Gets all conversations for the current user.
     *
     * @param page The page number (0-based)
     * @param size The page size
     * @return A page of conversation summaries
     */
    @GetMapping("/conversations")
    fun getConversations(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ConversationSummaryDto>> {
        val pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(chatMessageService.getUniqueConversations(pageable))
    }

    /**
     * Gets messages for a specific conversation.
     *
     * @param listingId The ID of the listing being discussed
     * @param otherUserId The ID of the other user in the conversation
     * @param page The page number (0-based)
     * @param size The page size
     * @return A page of chat messages
     */
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

    /**
     * Marks messages in a conversation as read.
     *
     * @param listingId The ID of the listing being discussed
     * @param otherUserId The ID of the other user in the conversation
     * @return No content response
     */
    @PostMapping("/read/{listingId}/{otherUserId}")
    fun markMessagesAsRead(
        @PathVariable listingId: Long,
        @PathVariable otherUserId: Long,
    ): ResponseEntity<Void> {
        chatMessageService.markMessagesAsRead(otherUserId, listingId)
        return ResponseEntity.noContent().build()
    }
}