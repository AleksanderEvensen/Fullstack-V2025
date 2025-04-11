package edu.ntnu.fullstack.amazoom.bookmark.entity

import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import jakarta.persistence.*

/**
 * Entity representing a bookmark for a listing.
 * Associates a user with a listing they have bookmarked.
 */
@Entity
@Table(
    name = "listing_bookmarks",
    uniqueConstraints = [UniqueConstraint(columnNames = ["listing_id", "user_id"])]
)
data class ListingBookmark(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listing_id", nullable = false)
    val listing: Listing,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
) {
    override fun toString(): String {
        return "ListingBookmark(id=$id, listingId=${listing.id}, userId=${user.id})"
    }
}