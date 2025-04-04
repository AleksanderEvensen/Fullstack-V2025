package edu.ntnu.fullstack.amazoom.auth.dto

import edu.ntnu.fullstack.amazoom.auth.entity.Address

data class UserDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: Address?,
    val profileImageUrl: String?,
)