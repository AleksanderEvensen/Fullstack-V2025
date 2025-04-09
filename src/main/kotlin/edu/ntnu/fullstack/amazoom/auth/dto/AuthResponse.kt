package edu.ntnu.fullstack.amazoom.auth.dto

/**
 * Data Transfer Object (DTO) for authentication responses.
 * Contains the JWT access token and a success message.
 */
data class AuthResponseDto(
    val accessToken: String,
    val message: String = "Authentication successful"
)