package edu.ntnu.fullstack.amazoom.auth.controller

import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponse
import edu.ntnu.fullstack.amazoom.auth.exception.*
import edu.ntnu.fullstack.amazoom.common.exception.MissingRoleException
import edu.ntnu.fullstack.amazoom.common.exception.UserAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom.auth"])
class AuthExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ex.message ?: "User already exists", HttpStatus.CONFLICT.value())
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }

    @ExceptionHandler(MissingRoleException::class)
    fun handleMissingRoleException(ex: MissingRoleException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                ex.message ?: "The required role is missing in the database",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ex.message ?: "Invalid credentials", HttpStatus.UNAUTHORIZED.value())
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidTokenException(ex: InvalidTokenException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ex.message ?: "Invalid token", HttpStatus.BAD_REQUEST.value())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(MissingTokenException::class)
    fun handleMissingTokenException(ex: MissingTokenException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ex.message ?: "Missing token", HttpStatus.BAD_REQUEST.value())
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun handleTokenExpiredException(ex: TokenExpiredException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ex.message ?: "Token has expired", HttpStatus.UNAUTHORIZED.value())
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.map {
            "${it.field}: ${it.defaultMessage}"
        }

        val errorResponse = ErrorResponse(
            message = "Validation failed",
            errors = errors,
            status = HttpStatus.BAD_REQUEST.value()
        )

        return ResponseEntity.badRequest().body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                ex.message ?: "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}