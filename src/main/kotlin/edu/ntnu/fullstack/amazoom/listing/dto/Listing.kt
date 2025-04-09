package edu.ntnu.fullstack.amazoom.listing.dto

import edu.ntnu.fullstack.amazoom.common.dto.UserDto
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.entity.ListingStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * Data Transfer Object (DTO) for a listing.
 * Contains all details about a product listing.
 */
@Schema(description = "Product listing information")
data class ListingDto(
    @field:Schema(description = "Unique identifier of the listing", example = "1")
    val id: Long,

    @field:Schema(description = "Title of the listing", example = "MacBook Pro 2022")
    val title: String,

    @field:Schema(description = "Category ID", example = "1")
    val categoryId: Long,

    @field:Schema(description = "Condition of the item", example = "VERY_GOOD")
    val condition: ListingCondition,

    @field:Schema(description = "Current price in local currency", example = "8500.0")
    val price: Double,

    @field:Schema(description = "Original price if discounted", example = "10000.0", nullable = true)
    val originalPrice: Double?,

    @field:Schema(description = "Detailed description of the item", example = "MacBook Pro 2022: In excellent condition. Barely used.")
    val description: String,

    @field:Schema(description = "Information about the seller")
    var seller: UserDto,

    @field:Schema(description = "Year the product was manufactured", example = "2022", nullable = true)
    val modelYear: Int?,

    @field:Schema(description = "Manufacturer/brand name", example = "Apple", nullable = true)
    val manufacturer: String?,

    @field:Schema(description = "Product model", example = "MacBook Pro", nullable = true)
    val model: String?,

    @field:Schema(description = "Product serial number", example = "C02G7RZRMD6M", nullable = true)
    val serialNumber: String?,

    @field:Schema(description = "Date of purchase in format YYYY-MM-DD", example = "2022-06-15", nullable = true)
    val purchaseDate: String?,

    @field:Schema(description = "Duration the item has been used", example = "6 months", nullable = true)
    val usageDuration: String?,

    @field:Schema(description = "List of any defects the item has", example = "[\"Minor scratch on bottom\"]")
    val defects: List<String>,

    @field:Schema(description = "List of any modifications made to the item", example = "[\"Upgraded RAM to 16GB\"]")
    val modifications: List<String>,

    @field:Schema(description = "Reason why the item is being sold", example = "Upgrading to a newer model", nullable = true)
    val reasonForSelling: String?,

    @field:Schema(description = "When the listing was created")
    val createdAt: LocalDateTime,

    @field:Schema(description = "Is the listing bookmarked by the user", example = "true")
    val isBookmarked: Boolean,

    @field:Schema(description = "Listing status", example = "ACTIVE")
    val status: ListingStatus,

    @field:Schema(description = "List of image URLs for this listing", example = "[\"macbook-12345.jpg\", \"macbook2-12345.jpg\"]")
    val images: List<String>
)