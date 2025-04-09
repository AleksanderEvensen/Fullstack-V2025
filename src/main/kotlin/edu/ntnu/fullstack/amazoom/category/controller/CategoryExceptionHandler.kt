package edu.ntnu.fullstack.amazoom.category.controller

import edu.ntnu.fullstack.amazoom.category.exception.CategoryNotFoundException
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


/**
 * Exception handler for category-related exceptions.
 * Provides appropriate HTTP responses for category errors.
 */
@RestControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom.category"])
class CategoryExceptionHandler {
    private val logger = LoggerFactory.getLogger(CategoryExceptionHandler::class.java)

    /**
     * Handles CategoryNotFoundException.
     *
     * @param ex The exception that was thrown
     * @return A 404 NOT FOUND response with error details
     */
    @ExceptionHandler(CategoryNotFoundException::class)
    fun handleCategoryNotFoundException(ex: CategoryNotFoundException): ResponseEntity<ErrorResponseDto> {
        logger.warn("Category not found: {}", ex.message)

        val errorResponse =
            ErrorResponseDto(ex.message ?: "Category does not exist", HttpStatus.NOT_FOUND.value())

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }
}