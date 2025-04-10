package edu.ntnu.fullstack.amazoom.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * Data Transfer Object (DTO) for user login requests.
 * Contains the credentials needed to authenticate a user.
 */
data class LoginRequestDto(
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password cannot be blank")
    @field:Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    val password: String,

    val redirectUrl: String? = null
)