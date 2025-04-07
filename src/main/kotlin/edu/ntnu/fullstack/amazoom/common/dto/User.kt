package edu.ntnu.fullstack.amazoom.common.dto

import java.util.UUID

data class UserDto (
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String?,
)

data class CreateUserDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
)