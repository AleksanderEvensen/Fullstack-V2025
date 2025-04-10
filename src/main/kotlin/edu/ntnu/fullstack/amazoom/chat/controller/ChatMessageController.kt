package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageRequestDto
import edu.ntnu.fullstack.amazoom.chat.dto.ConversationSummaryDto
import edu.ntnu.fullstack.amazoom.chat.service.ChatMessageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST Controller for handling chat messages.
 * Provides endpoints for sending messages and retrieving conversations.
 * conversations are between the current user and other users about a given listing.
 */
@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "Operations for chat messaging")
class ChatMessageController(
    private val chatMessageService: ChatMessageService
) {
    /**
     * Gets all conversations for the current user.
     */
    @Operation(
        summary = "Get user conversations",
        description = "Retrieves all conversations for the current user"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Conversations retrieved successfully",
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated"
        )
    )
    @GetMapping("/conversations")
    fun getConversations(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<ConversationSummaryDto>> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"))
        val conversations = chatMessageService.getConversations(pageable)
        return ResponseEntity.ok(conversations)
    }

    /**
     * Gets messages for a specific conversation.
     */
    @Operation(
        summary = "Get conversation messages",
        description = "Retrieves messages between the current user and another user for a specific listing"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Messages retrieved successfully",
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated"
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
     * Sends a new message.
     */
    @Operation(
        summary = "Send message",
        description = "Sends a new message to another user about a listing"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Message sent successfully",
            content = [Content(schema = Schema(implementation = ChatMessageDto::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid message"
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated"
        ),
        ApiResponse(
            responseCode = "404",
            description = "Recipient or listing not found"
        )
    )
    @PostMapping("/send")
    fun sendMessage(@Valid @RequestBody request: ChatMessageRequestDto): ResponseEntity<ChatMessageDto> {
        val message = chatMessageService.sendMessage(request)
        return ResponseEntity.ok(message)
    }
}