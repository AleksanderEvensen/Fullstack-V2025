package edu.ntnu.fullstack.amazoom.common.controller

import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import edu.ntnu.fullstack.amazoom.common.exception.MissingRoleException
import edu.ntnu.fullstack.amazoom.common.exception.UnauthorizedException
import edu.ntnu.fullstack.amazoom.common.exception.UserAlreadyExistsException
import edu.ntnu.fullstack.amazoom.common.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom"])
class CommonExceptionHandler {
    // This class can be used to handle common exceptions across the application.
    // You can add methods here to handle specific exceptions and return custom error responses.
    // For example, you can handle NotFoundException, BadRequestException, etc.
    // Each method can return a ResponseEntity with a custom error response.

    /**
     * Handles exception when a required role is missing from the database.
     */
    @ExceptionHandler(MissingRoleException::class)
    fun handleMissingRoleException(ex: MissingRoleException): ResponseEntity<ErrorResponseDto> {
        val errorResponse =
            ErrorResponseDto(
                ex.message ?: "The required role is missing in the database",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    /**
     * Handles validation exceptions for request bodies.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponseDto> {
        val errors = ex.bindingResult.fieldErrors.map {
            "${it.field}: ${it.defaultMessage}"
        }

        val errorResponse = ErrorResponseDto(
            message = "Validation failed",
            errors = errors,
            status = HttpStatus.BAD_REQUEST.value()
        )

        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Handles exception when a user is not found in the database.
     */
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<ErrorResponseDto> {
        val errorResponse =
            ErrorResponseDto(
                ex.message ?: "User not found",
                HttpStatus.NOT_FOUND.value()
            )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * Handles exception when a user is not found in the database.
     */
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<ErrorResponseDto> {
        val errorResponse =
            ErrorResponseDto(
                ex.message ?: "User not found",
                HttpStatus.NOT_FOUND.value()
            )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * Handles exception when a user already exists in the database.
     */
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException): ResponseEntity<ErrorResponseDto> {
        val errorResponse =
            ErrorResponseDto(
                ex.message ?: "User already exists",
                HttpStatus.CONFLICT.value()
            )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }

    /**
     * Handles exception when a user is not authorized to perform an action.
     */
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<ErrorResponseDto> {
        val errorResponse =
            ErrorResponseDto(ex.message ?: "Invalid credentials", HttpStatus.UNAUTHORIZED.value())
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    /**
     * Handles exception when a user is not authorized to perform an action.
     */
    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<ErrorResponseDto> {
        val errorResponse =
            ErrorResponseDto(
                ex.message ?: "Unauthorized access",
                HttpStatus.UNAUTHORIZED.value()
            )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    /**
     * Handles exception when a user lacks the required role/authority.
     */
    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(ex: AuthorizationDeniedException): ResponseEntity<ErrorResponseDto> {
        val errorResponse = ErrorResponseDto(
            message = "Access denied: insufficient privileges",
            status = HttpStatus.FORBIDDEN.value()
        )
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponseDto> {
        val errorResponse =
            ErrorResponseDto(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}