package edu.ntnu.fullstack.amazoom.chat.controller

import edu.ntnu.fullstack.amazoom.chat.exception.*
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Exception handler for chat-related exceptions.
 * Provides appropriate HTTP responses for chat errors.
 */
@RestControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom.chat"])
class ChatExceptionHandler {
    private val logger = LoggerFactory.getLogger(ChatExceptionHandler::class.java)

    /**
     * Handles MessageNotFoundException.
     *
     * @param ex The exception that was thrown
     * @return A 404 NOT FOUND response with error details
     */
    @ExceptionHandler(MessageNotFoundException::class)
    fun handleMessageNotFoundException(ex: MessageNotFoundException): ResponseEntity<ErrorResponseDto> {
        logger.warn("Message not found: {}", ex.message)

        val errorResponse = ErrorResponseDto(
            message = ex.message ?: "Message not found",
            status = HttpStatus.NOT_FOUND.value()
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * Handles MessageAccessDeniedException.
     *
     * @param ex The exception that was thrown
     * @return A 403 FORBIDDEN response with error details
     */
    @ExceptionHandler(MessageAccessDeniedException::class)
    fun handleMessageAccessDeniedException(ex: MessageAccessDeniedException): ResponseEntity<ErrorResponseDto> {
        logger.warn("Message access denied: {}", ex.message)

        val errorResponse = ErrorResponseDto(
            message = ex.message ?: "You don't have permission to access this message",
            status = HttpStatus.FORBIDDEN.value()
        )

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
    }

    /**
     * Handles RecipientNotFoundException.
     *
     * @param ex The exception that was thrown
     * @return A 404 NOT FOUND response with error details
     */
    @ExceptionHandler(RecipientNotFoundException::class)
    fun handleRecipientNotFoundException(ex: RecipientNotFoundException): ResponseEntity<ErrorResponseDto> {
        logger.warn("Recipient not found: {}", ex.message)

        val errorResponse = ErrorResponseDto(
            message = ex.message ?: "Recipient not found",
            status = HttpStatus.NOT_FOUND.value()
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * Handles InvalidMessageException.
     *
     * @param ex The exception that was thrown
     * @return A 400 BAD REQUEST response with error details
     */
    @ExceptionHandler(InvalidMessageException::class)
    fun handleInvalidMessageException(ex: InvalidMessageException): ResponseEntity<ErrorResponseDto> {
        logger.warn("Invalid message: {}", ex.message)

        val errorResponse = ErrorResponseDto(
            message = ex.message ?: "Invalid message",
            status = HttpStatus.BAD_REQUEST.value()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    /**
     * Handles MessageTooLongException.
     *
     * @param ex The exception that was thrown
     * @return A 400 BAD REQUEST response with error details
     */
    @ExceptionHandler(MessageTooLongException::class)
    fun handleMessageTooLongException(ex: MessageTooLongException): ResponseEntity<ErrorResponseDto> {
        logger.warn("Message too long: {}", ex.message)

        val errorResponse = ErrorResponseDto(
            message = ex.message ?: "Message exceeds maximum allowed length",
            status = HttpStatus.BAD_REQUEST.value()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    /**
     * Handles ChatException.
     *
     * @param ex The exception that was thrown
     * @return A 500 INTERNAL SERVER ERROR response with error details
     */
    @ExceptionHandler(ChatException::class)
    fun handleChatException(ex: ChatException): ResponseEntity<ErrorResponseDto> {
        logger.error("Chat error: {}", ex.message)

        val errorResponse = ErrorResponseDto(
            message = ex.message ?: "An error occurred with the chat service",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}