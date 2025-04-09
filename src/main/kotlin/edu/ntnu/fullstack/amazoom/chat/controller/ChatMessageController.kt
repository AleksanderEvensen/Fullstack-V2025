package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.dto.ConversationSummaryDto
import edu.ntnu.fullstack.amazoom.chat.service.ChatMessageService
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Chat", description = "Operations for managing chat messages and conversations")
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
    @Operation(
        summary = "Get all conversations",
        description = "Gets all conversations for the currently authenticated user"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Conversations retrieved successfully",
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
        )
    )
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
    @Operation(
        summary = "Get conversation messages",
        description = "Gets messages for a specific conversation between the current user and another user about a listing"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Messages retrieved successfully",
            content = [Content(schema = Schema(implementation = ChatMessageDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "403",
            description = "User not authorized to access this conversation",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Mark messages as read",
        description = "Marks all messages in a conversation as read by the current user"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "204",
            description = "Messages marked as read successfully"
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
    @PostMapping("/read/{listingId}/{otherUserId}")
    fun markMessagesAsRead(
        @PathVariable listingId: Long,
        @PathVariable otherUserId: Long,
    ): ResponseEntity<Void> {
        chatMessageService.markMessagesAsRead(otherUserId, listingId)
        return ResponseEntity.noContent().build()
    }
}