package edu.ntnu.fullstack.amazoom.common.dto

data class ErrorResponse (
    val message: String,
    val status: Int,
    val errors: List<String>? = null,
)