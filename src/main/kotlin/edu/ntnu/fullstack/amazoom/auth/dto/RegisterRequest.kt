package edu.ntnu.fullstack.amazoom.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * Data Transfer Object (DTO) for user registration requests.
 * Contains all required fields to register a new user in the system.
 */
data class RegisterRequestDto(
    @field:NotBlank(message = "Full Name is required")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String,

    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String,

    @field:NotBlank(message = "Street name and number is required")
    val street: String,

    @field:NotBlank(message = "City name is required")
    val city: String,

    @field:NotBlank(message = "Postal code is required")
    val postalCode: String,

)