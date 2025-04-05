package edu.ntnu.fullstack.amazoom.category.controller

import edu.ntnu.fullstack.amazoom.category.exception.CategoryNotFoundException
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom.category"])
class CategoryExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException::class)
    fun handleCategoryNotFoundException(ex: CategoryNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(ex.message ?: "Category does not exist", HttpStatus.CONFLICT.value())
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }
}