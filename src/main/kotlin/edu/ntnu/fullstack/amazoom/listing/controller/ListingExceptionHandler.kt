package edu.ntnu.fullstack.amazoom.listing.controller

import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponse
import edu.ntnu.fullstack.amazoom.listing.exception.ListingNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom"])
class ListingExceptionHandler {

    @ExceptionHandler(ListingNotFoundException::class)
    fun handleListingNotFoundException(ex: ListingNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ex.message ?: "Listing does not exist", HttpStatus.CONFLICT.value())
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }
}