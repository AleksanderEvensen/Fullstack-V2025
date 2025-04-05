package edu.ntnu.fullstack.amazoom.bookmark.mapper

import edu.ntnu.fullstack.amazoom.auth.entity.User
import edu.ntnu.fullstack.amazoom.bookmark.dto.ListingBookmarkResponse
import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.mapper.ListingMapper

object ListingBookmarkMapper {

    /**
     * Convert create/update request + Listing + User -> ListingBookmark entity.
     *
     * Typically only used when *creating* a new bookmark,
     * since you'd just store the user, listing, and the current Instant.
     */
    fun toEntity(
        listing: Listing,
        user: User
    ): ListingBookmark {
        return ListingBookmark(
            listing = listing,
            user = user,
        )
    }

    /**
     * Convert entity -> response DTO.
     */
    fun toResponse(entity: ListingBookmark): ListingBookmarkResponse {
        return ListingBookmarkResponse(
            id = entity.id,
            listing = ListingMapper.toResponseDto(entity.listing),
        )
    }
}
