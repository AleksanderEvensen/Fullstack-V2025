package edu.ntnu.fullstack.amazoom.common.dto

data class AddressDto (
    val streetName: String,
    val streetNumber: String,
    val postalCode: String,
    val city: String,
    val country: String,
)