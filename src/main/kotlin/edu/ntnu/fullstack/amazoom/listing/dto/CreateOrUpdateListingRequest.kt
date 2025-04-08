package edu.ntnu.fullstack.amazoom.listing.dto

import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition

data class CreateOrUpdateListingRequest(
    val title: String,
    val categoryId: Long,
    val condition: ListingCondition,
    val price: Double,
    val originalPrice: Double? = null,
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

    // Images
    val images: List<String>? = null
)
