package edu.ntnu.fullstack.amazoom.common.dto

/**
 * Data Transfer Object (DTO) for error responses.
 *
 * @property message The error message.
 * @property status The HTTP status code.
 * @property errors Optional list of specific error messages.
 */
data class ErrorResponseDto(
    val message: String,
    val status: Int,
    val errors: List<String>? = null,
)