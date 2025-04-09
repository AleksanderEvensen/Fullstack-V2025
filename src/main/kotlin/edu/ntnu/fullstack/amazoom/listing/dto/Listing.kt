package edu.ntnu.fullstack.amazoom.listing.dto

import edu.ntnu.fullstack.amazoom.common.dto.UserDto
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import java.time.LocalDateTime

/**
 * Data Transfer Object (DTO) for a listing.
 * Contains all details about a product listing.
 */
data class ListingDto(
    val id: Long,
    val title: String,
    val categoryId: Long,
    val condition: ListingCondition,
    val price: Double,
    val originalPrice: Double?,
    val description: String,
    var seller: UserDto,
    val modelYear: String?,
    val manufacturer: String?,
    val model: String?,
    val serialNumber: String?,
    val purchaseDate: String?,
    val usageDuration: String?,
    val defects: List<String>,
    val modifications: List<String>,
    val reasonForSelling: String?,
    val createdAt: LocalDateTime,
    val images: List<String>
)
