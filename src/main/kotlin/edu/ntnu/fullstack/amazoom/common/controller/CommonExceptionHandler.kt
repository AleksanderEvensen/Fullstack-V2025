package edu.ntnu.fullstack.amazoom.common.controller

import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom"])
class CommonExceptionHandler {
    // This class can be used to handle common exceptions across the application.
    // You can add methods here to handle specific exceptions and return custom error responses.
    // For example, you can handle NotFoundException, BadRequestException, etc.
    // Each method can return a ResponseEntity with a custom error response.

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}