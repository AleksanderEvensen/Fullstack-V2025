package edu.ntnu.fullstack.amazoom.common.dto

import edu.ntnu.fullstack.amazoom.common.entity.Address
import java.util.UUID

data class UserDto (
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String?,
    val address: Address?,
)

data class CreateUserDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
)