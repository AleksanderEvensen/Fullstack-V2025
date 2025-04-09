package edu.ntnu.fullstack.amazoom.bookmark.dto

import edu.ntnu.fullstack.amazoom.listing.dto.ListingDto
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

/**
 * Data Transfer Object (DTO) for a listing bookmark response.
 * Contains information about a bookmark and its associated listing.
 */
data class ListingBookmarkResponseDto(
    val id: Long,
    val listing: ListingDto
)

/**
 * Data Transfer Object (DTO) for creating a new listing bookmark.
 * Contains the ID of the listing to bookmark.
 */
data class CreateListingBookmarkRequestDto(
    @field:NotNull(message = "Listing ID is required")
    @field:Positive(message = "Listing ID must be positive")
    val listingId: Long
)