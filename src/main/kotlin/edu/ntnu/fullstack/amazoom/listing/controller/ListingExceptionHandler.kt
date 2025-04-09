package edu.ntnu.fullstack.amazoom.listing.controller

import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import edu.ntnu.fullstack.amazoom.listing.exception.ListingNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom"])
class ListingExceptionHandler {
    private val logger = LoggerFactory.getLogger(ListingExceptionHandler::class.java)

    /**
     * Handles ListingNotFoundException.
     *
     * @param ex The exception that was thrown
     * @return A 404 NOT FOUND response with error details
     */
    @ExceptionHandler(ListingNotFoundException::class)
    fun handleListingNotFoundException(ex: ListingNotFoundException): ResponseEntity<ErrorResponseDto> {
        logger.warn("Listing not found: {}", ex.message)
        val errorResponse =
            ErrorResponseDto(ex.message ?: "Listing does not exist", HttpStatus.NOT_FOUND.value())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }
}