package edu.ntnu.fullstack.amazoom.common.dto

/**
 * Data Transfer Object (DTO) for updating user profile address information.
 */
data class UpdateAddressRequestDto(
    val streetName: String,
    val streetNumber: String,
    val postalCode: String,
    val city: String,
    val country: String
)

/**
 * Data Transfer Object (DTO) for updating user profile information.
 * This is the response sent back to the client after a successful update.
 */
data class UpdateProfileResponseDto(
    val message: String
)