package edu.ntnu.fullstack.amazoom.auth.dto

data class ErrorResponse (
    val message: String,
    val status: Int,
)