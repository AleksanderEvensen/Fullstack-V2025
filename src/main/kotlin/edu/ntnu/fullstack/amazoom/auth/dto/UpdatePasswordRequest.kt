package edu.ntnu.fullstack.amazoom.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * Data transfer object (DTO) for updating a user's password.
 * Contains the current password and the new password.
 */
data class UpdatePasswordRequestDto(
    @field:NotBlank(message = "Current password is required")
    val currentPassword: String,

    @field:NotBlank(message = "New password is required")
    @field:Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    val newPassword: String
)