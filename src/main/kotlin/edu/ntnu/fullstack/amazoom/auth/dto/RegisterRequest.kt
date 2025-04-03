package edu.ntnu.fullstack.amazoom.auth.dto

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String
)