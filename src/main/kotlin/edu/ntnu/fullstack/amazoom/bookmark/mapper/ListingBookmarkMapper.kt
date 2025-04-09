package edu.ntnu.fullstack.amazoom.bookmark.mapper

import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.bookmark.dto.ListingBookmarkResponseDto
import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.mapper.ListingMapper

/**
 * Mapper class for converting between ListingBookmark entities and DTOs.
 */
object ListingBookmarkMapper {

    /**
     * Converts a Listing and User to a ListingBookmark entity.
     * Used when creating a new bookmark.
     *
     * @param listing The listing to bookmark
     * @param user The user creating the bookmark
     * @return A new ListingBookmark entity
     */
    fun toEntity(
        listing: Listing,
        user: User
    ): ListingBookmark {
        return ListingBookmark(
            listing = listing,
            user = user
        )
    }

    /**
     * Converts a ListingBookmark entity to a ListingBookmarkResponseDto.
     *
     * @param entity The ListingBookmark entity to convert
     * @return The resulting ListingBookmarkResponseDto
     */
    fun toResponseDto(entity: ListingBookmark): ListingBookmarkResponseDto {
        return ListingBookmarkResponseDto(
            id = entity.id,
            listing = ListingMapper.toResponseDto(entity.listing)
        )
    }
}