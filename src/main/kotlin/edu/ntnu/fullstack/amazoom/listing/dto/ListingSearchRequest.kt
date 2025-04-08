package edu.ntnu.fullstack.amazoom.listing.dto

import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition

data class ListingSearchRequest(
    val q: String? = null,
    val categoryId: Long? = null,
    val categoryName: String? = null,
    val condition: ListingCondition? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minModelYear: Int? = null,
    val maxModelYear: Int? = null,
    val manufacturer: String? = null,
    val model: String? = null,
    val sellerId: Long? = null,
    val defectsCount: Int? = null,
    val modificationsCount: Int? = null,
    val page: Int = 0,
    val size: Int = 20,
    val sortBy: String = "createdAt",
    val sortDirection: String = "DESC"
) 