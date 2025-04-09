package edu.ntnu.fullstack.amazoom.bookmark.controller

import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkAlreadyExistsException
import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkOwnListingException
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


/**
 * Exception handler for bookmark-related exceptions.
 * Provides appropriate HTTP responses for bookmark errors.
 */
@RestControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom.bookmark"])
class ListingBookmarkExceptionHandler {
    private val logger = LoggerFactory.getLogger(ListingBookmarkExceptionHandler::class.java)

    /**
     * Handles BookmarkOwnListingException.
     *
     * @param ex The exception that was thrown
     * @return A 400 BAD REQUEST response with error details
     */
    @ExceptionHandler(BookmarkOwnListingException::class)
    fun handleBookmarkOwnListingException(ex: BookmarkOwnListingException): ResponseEntity<ErrorResponseDto> {
        logger.warn("Cannot bookmark own listing: {}", ex.message)

        val errorResponse =
            ErrorResponseDto(ex.message ?: "You cannot bookmark your own listing", HttpStatus.BAD_REQUEST.value())

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    /**
     * Handles BookmarkAlreadyExistsException.
     *
     * @param ex The exception that was thrown
     * @return A 409 CONFLICT response with error details
     */
    @ExceptionHandler(BookmarkAlreadyExistsException::class)
    fun handleBookmarkAlreadyExists(ex: BookmarkAlreadyExistsException): ResponseEntity<ErrorResponseDto> {
        logger.warn("Bookmark already exists: {}", ex.message)

        val errorResponse =
            ErrorResponseDto(ex.message ?: "Bookmark already exists", HttpStatus.CONFLICT.value())

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }
}