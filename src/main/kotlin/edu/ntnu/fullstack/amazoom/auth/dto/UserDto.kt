package edu.ntnu.fullstack.amazoom.auth.dto

import edu.ntnu.fullstack.amazoom.common.entity.Address
import java.util.UUID

data class UserDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: Address?,
    val profileImageUrl: String?,
)