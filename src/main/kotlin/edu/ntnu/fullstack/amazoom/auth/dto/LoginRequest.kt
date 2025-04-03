package edu.ntnu.fullstack.amazoom.auth.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    val email: String,
    val password: String,
)