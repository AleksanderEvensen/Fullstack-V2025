package edu.ntnu.fullstack.amazoom.common.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data Transfer Object (DTO) for user address information.
 */
@Schema(description = "User address information")
data class AddressDto (
    @field:Schema(description = "Street name", example = "Main Street")
    val streetName: String,

    @field:Schema(description = "Street number", example = "123")
    val streetNumber: String,

    @field:Schema(description = "Postal code", example = "12345")
    val postalCode: String,

    @field:Schema(description = "City name", example = "Oslo")
    val city: String,

    @field:Schema(description = "Country name", example = "Norway")
    val country: String,
)