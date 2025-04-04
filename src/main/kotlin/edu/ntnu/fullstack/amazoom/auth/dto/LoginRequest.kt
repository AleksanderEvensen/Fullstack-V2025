package edu.ntnu.fullstack.amazoom.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password cannot be blank")
    @field:Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    val password: String,
)