package edu.ntnu.fullstack.amazoom.common.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data Transfer Object (DTO) for basic user information.
 * Contains non-sensitive user data for public display.
 */
@Schema(description = "Basic user information for public display")
data class UserDto (
    @field:Schema(description = "Unique identifier of the user", example = "1")
    val id: Long,

    @field:Schema(description = "User's first name", example = "John")
    val firstName: String,

    @field:Schema(description = "User's last name", example = "Doe")
    val lastName: String,

    @field:Schema(description = "URL to user's profile image", example = "profile-1234-abcd.jpg", nullable = true)
    val profileImageUrl: String?,

    @field:Schema(description = "User's address information", nullable = true)
    val address: AddressDto?,
)

/**
 * Data Transfer Object (DTO) for detailed user information.
 * Contains complete user data for profile views.
 */
@Schema(description = "Complete user profile information")
data class FullUserDto (
    @field:Schema(description = "Unique identifier of the user", example = "1")
    val id: Long,

    @field:Schema(description = "User's first name", example = "John")
    val firstName: String,

    @field:Schema(description = "User's last name", example = "Doe")
    val lastName: String,

    @field:Schema(description = "User's email address", example = "john.doe@example.com")
    val email: String,

    @field:Schema(description = "User's phone number", example = "+4712345678")
    val phoneNumber: String,

    @field:Schema(description = "URL to user's profile image", example = "profile-1234-abcd.jpg", nullable = true)
    val profileImageUrl: String?,

    @field:Schema(description = "User's address information", nullable = true)
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