package edu.ntnu.fullstack.amazoom.auth.dto

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val message: String = "Authentication successful"
)