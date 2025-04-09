package edu.ntnu.fullstack.amazoom.common.dto

/**
 * Data Transfer Object (DTO) for basic user information.
 * Contains non-sensitive user data for public display.
 */
data class UserDto (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String?,
    val address: AddressDto?,
)

/**
 * Data Transfer Object (DTO) for detailed user information.
 * Contains complete user data for profile views.
 */
data class FullUserDto (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val profileImageUrl: String?,
    val address: AddressDto?,
)

/**
 * Data Transfer Object (DTO) for creating a new user.
 * Contains all required fields to create a user.
 */
data class CreateUserDto(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
)