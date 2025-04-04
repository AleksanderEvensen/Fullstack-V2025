package edu.ntnu.fullstack.amazoom.listing.dto

import edu.ntnu.fullstack.amazoom.auth.dto.UserDto
import edu.ntnu.fullstack.amazoom.auth.entity.User
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition


data class ListingResponse(
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

    // Images
    val images: List<String>
)
