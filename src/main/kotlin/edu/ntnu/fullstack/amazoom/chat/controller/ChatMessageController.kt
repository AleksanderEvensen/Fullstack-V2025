package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageDto
import edu.ntnu.fullstack.amazoom.chat.dto.ChatMessageRequestDto
import edu.ntnu.fullstack.amazoom.chat.dto.ConversationSummaryDto
import edu.ntnu.fullstack.amazoom.chat.service.ChatMessageService
import edu.ntnu.fullstack.amazoom.common.service.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.async.DeferredResult
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "Operations for chat messaging")
class ChatController(
    private val chatMessageService: ChatMessageService,
    private val userService: UserService
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
        val result = DeferredResult<ResponseEntity<List<ChatMessageDto>>>(30000L) // 30 seconds timeout
        val lastMessageTime = Instant.ofEpochMilli(lastTimestamp)

        val currentUser = userService.getCurrentUser()

        logger.debug("Starting poll for user: {}, listingId: {}, otherUserId: {}",
            currentUser.email, listingId, otherUserId)

        val executor = Executors.newSingleThreadScheduledExecutor()

        val task = object : Runnable {
            override fun run() {
                try {
                    val messages = chatMessageService.checkForNewMessages(
                        currentUser,
                        lastMessageTime,
                        otherUserId,
                        listingId
                    )

                    if (messages.isNotEmpty()) {
                        result.setResult(ResponseEntity.ok(messages))
                        return
                    }

                    if (!result.isSetOrExpired) {
                        executor.schedule(this, 500, TimeUnit.MILLISECONDS)
                    }
                } catch (e: Exception) {
                    logger.error("Error checking for messages: {}", e.message, e)
                    result.setErrorResult(ResponseEntity.status(500).body(emptyList<ChatMessageDto>()))
                }
            }
        }

        executor.schedule(task, 0, TimeUnit.MILLISECONDS)

        result.onCompletion {
            executor.shutdown()
        }

        return result
    }
}