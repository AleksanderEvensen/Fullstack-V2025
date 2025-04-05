package edu.ntnu.fullstack.amazoom.bookmark.dto

import edu.ntnu.fullstack.amazoom.listing.dto.ListingResponse

data class ListingBookmarkResponse(
    val id: Long,
    val listing: ListingResponse,
)