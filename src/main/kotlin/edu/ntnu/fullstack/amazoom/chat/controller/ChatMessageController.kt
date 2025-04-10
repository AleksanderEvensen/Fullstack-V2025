package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageRequestDto
import edu.ntnu.fullstack.amazoom.chat.dto.ConversationSummaryDto
import edu.ntnu.fullstack.amazoom.chat.service.ChatMessageService
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult
import java.time.Instant
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "Operations for chat messaging")
class ChatController(
    private val chatMessageService: ChatMessageService
) {
    private val logger = LoggerFactory.getLogger(ChatController::class.java)
    private val LONG_POLL_TIMEOUT = 30000L

    /**
     * Gets all conversations for the current user.
     */
    @GetMapping("/conversations")
    fun getConversations(): ResponseEntity<List<ConversationSummaryDto>> {
        val conversations = chatMessageService.getUniqueConversations()

        return ResponseEntity.ok(conversations)
    }

    /**
     * Gets messages for a specific conversation.
     */
    @GetMapping("/conversation/{listingId}/{otherUserId}")
    fun getConversation(
        @PathVariable listingId: Long,
        @PathVariable otherUserId: Long
    ): ResponseEntity<List<ChatMessageDto>> {
        return ResponseEntity.ok(
            chatMessageService.getMessagesForConversation(
                otherUserId,
                listingId
            )
        )
    }

    /**
     * Sends a new message.
     */
    @PostMapping("/send")
    fun sendMessage(@Valid @RequestBody request: ChatMessageRequestDto): ResponseEntity<ChatMessageDto> {
        val message = chatMessageService.sendMessage(request)
        return ResponseEntity.ok(message)
    }

    /**
     * Long polling endpoint to receive new messages for a specific conversation.
     */
    @GetMapping("/poll/{listingId}/{otherUserId}")
    fun pollForMessages(
        @PathVariable otherUserId: Long,
        @PathVariable listingId: Long,
        @RequestParam("lastTimestamp") lastTimestamp: Long
    ): DeferredResult<ResponseEntity<List<ChatMessageDto>>> {
        val result = DeferredResult<ResponseEntity<List<ChatMessageDto>>>(LONG_POLL_TIMEOUT + 1000L)
        val lastMessageTime = Instant.ofEpochMilli(lastTimestamp)

        result.onTimeout {
            result.setResult(ResponseEntity.ok(emptyList()))
        }

        val securityContext = SecurityContextHolder.getContext()
        val runnable = DelegatingSecurityContextRunnable(Runnable {
            try {
                val messages = chatMessageService.pollForMessages(
                    lastMessageTime,
                    otherUserId,
                    listingId
                )
                result.setResult(ResponseEntity.ok(messages))
            } catch (e: Exception) {
                logger.error("Error during long polling: {}", e.message)
                result.setErrorResult(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(emptyList<ChatMessageDto>())
                )
            }
        }, securityContext)

        CompletableFuture.runAsync(runnable)

        return result
    }
}