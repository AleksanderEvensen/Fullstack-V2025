package edu.ntnu.fullstack.amazoom.listing.dto

import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

/**
 * Data Transfer Object (DTO) for creating or updating a listing.
 * Contains all fields that can be set when creating or updating a listing.
 */
@Schema(description = "Request for creating or updating a listing")
data class CreateOrUpdateListingRequestDto(
    @field:Schema(description = "Title of the listing", example = "MacBook Pro 2022", required = true)
    @field:NotBlank(message = "Title is required")
    val title: String,

    @field:Schema(description = "Category ID", example = "1", required = true)
    @field:NotNull(message = "Category ID is required")
    val categoryId: Long,

    @field:Schema(description = "Condition of the item", example = "VERY_GOOD", required = true)
    @field:NotNull(message = "Condition is required")
    val condition: ListingCondition,

    @field:Schema(description = "Current price in local currency", example = "8500.0", required = true)
    @field:NotNull(message = "Price is required")
    @field:Positive(message = "Price must be positive")
    val price: Double,

    @field:Schema(description = "Original price if discounted", example = "10000.0", nullable = true)
    val originalPrice: Double? = null,

    @field:Schema(description = "Detailed description of the item", example = "MacBook Pro 2022: In excellent condition. Barely used.", required = true)
    @field:NotBlank(message = "Description is required")
    val description: String,

    // Product Details
    @field:Schema(description = "Year the product was manufactured", example = "2022", nullable = true)
    val modelYear: Int? = null,

    @field:Schema(description = "Manufacturer/brand name", example = "Apple", nullable = true)
    val manufacturer: String? = null,

    @field:Schema(description = "Product model", example = "MacBook Pro", nullable = true)
    val model: String? = null,

    @field:Schema(description = "Product serial number", example = "C02G7RZRMD6M", nullable = true)
    val serialNumber: String? = null,

    @field:Schema(description = "Date of purchase in format YYYY-MM-DD", example = "2022-06-15", nullable = true)
    val purchaseDate: String? = null,

    @field:Schema(description = "Duration the item has been used", example = "6 months", nullable = true)
    val usageDuration: String? = null,

    @field:Schema(description = "List of any defects the item has", example = "[\"Minor scratch on bottom\"]", nullable = true)
    val defects: List<String>? = null,

    @field:Schema(description = "List of any modifications made to the item", example = "[\"Upgraded RAM to 16GB\"]", nullable = true)
    val modifications: List<String>? = null,

    @field:Schema(description = "Reason why the item is being sold", example = "Upgrading to a newer model", nullable = true)
    val reasonForSelling: String? = null,

    @field:Schema(description = "List of image URLs for this listing", example = "[\"macbook-12345.jpg\", \"macbook2-12345.jpg\"]", nullable = true)
    val images: List<String>? = null
)