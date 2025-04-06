package edu.ntnu.fullstack.amazoom.bookmark.controller

import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkAlreadyExists
import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkOwnListingException
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom"])
class ListingBookmarkExceptionHandler {

    @ExceptionHandler(BookmarkOwnListingException::class)
    fun handleBookmarkOwnListingException(ex: BookmarkOwnListingException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ex.message ?: "You cannot bookmark your own listing", HttpStatus.CONFLICT.value())
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }

    @ExceptionHandler(BookmarkAlreadyExists::class)
    fun handleBookmarkAlreadyExists(ex: BookmarkAlreadyExists): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ex.message ?: "Bookmark already exists", HttpStatus.CONFLICT.value())
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }
}