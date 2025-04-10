package edu.ntnu.fullstack.amazoom.listing.dto

import edu.ntnu.fullstack.amazoom.listing.entity.ListingStatus

data class MyListingRequest(
    val page: Int = 0,
    val size: Int = 20,
    val sortBy: String = "createdAt",
    val sortDirection: String = "DESC",
    val status: ListingStatus = ListingStatus.ACTIVE,
)