package edu.ntnu.fullstack.amazoom.bookmark.repository


import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ListingBookmarkRepository : JpaRepository<ListingBookmark, Long> {
    fun existsByUserIdAndListingId(userId: Long, listingId: Long): Boolean
}