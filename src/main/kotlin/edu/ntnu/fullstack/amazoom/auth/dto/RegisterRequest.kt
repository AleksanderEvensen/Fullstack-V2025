package edu.ntnu.fullstack.amazoom.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    val password: String,

    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String
)