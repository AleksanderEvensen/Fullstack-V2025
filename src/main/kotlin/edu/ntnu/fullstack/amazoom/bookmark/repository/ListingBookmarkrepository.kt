package edu.ntnu.fullstack.amazoom.bookmark.repository


import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ListingBookmarkRepository : JpaRepository<ListingBookmark, Long> {
    fun existsByUserIdAndListingId(userId: Long, listingId: Long): Boolean

    @Query("SELECT lb FROM ListingBookmark lb WHERE lb.user.id = :userId")
    fun findAllForUser(userId: Long): List<ListingBookmark>
}