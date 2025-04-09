package edu.ntnu.fullstack.amazoom.listing.dto

import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

/**
 * Data Transfer Object (DTO) for creating or updating a listing.
 * Contains all fields that can be set when creating or updating a listing.
 */
data class CreateOrUpdateListingRequestDto(
    @field:NotBlank(message = "Title is required")
    val title: String,

    @field:NotNull(message = "Category ID is required")
    val categoryId: Long,

    @field:NotNull(message = "Condition is required")
    val condition: ListingCondition,

    @field:NotNull(message = "Price is required")
    @field:Positive(message = "Price must be positive")
    val price: Double,

    val originalPrice: Double? = null,

    @field:NotBlank(message = "Description is required")
    val description: String,

    // Product Details
    val modelYear: Int? = null,
    val manufacturer: String? = null,
    val model: String? = null,
    val serialNumber: String? = null,
    val purchaseDate: String? = null,
    val usageDuration: String? = null,
    val defects: List<String>? = null,
    val modifications: List<String>? = null,
    val reasonForSelling: String? = null,
    val images: List<String>? = null
)