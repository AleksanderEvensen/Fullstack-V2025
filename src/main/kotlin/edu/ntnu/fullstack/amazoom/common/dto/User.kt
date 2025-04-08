package edu.ntnu.fullstack.amazoom.common.dto

import java.util.UUID

data class UserDto (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String?,
)

data class FullUserDto (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val profileImageUrl: String?,
    val address: AddressDto?,
)

data class CreateUserDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
)