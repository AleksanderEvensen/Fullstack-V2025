package edu.ntnu.fullstack.amazoom.auth.controller

import edu.ntnu.fullstack.amazoom.auth.exception.InvalidCredentialsException
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom.auth"])
class AuthExceptionHandler {

    /**
     * Handles exception when invalid credentials are provided.
     */
    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentialsException(
        ex: InvalidCredentialsException
    ): ResponseEntity<ErrorResponseDto> {
        val errorResponse = ErrorResponseDto(
            message = ex.message ?: "Invalid credentials",
            status = HttpStatus.UNAUTHORIZED.value()
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }
}