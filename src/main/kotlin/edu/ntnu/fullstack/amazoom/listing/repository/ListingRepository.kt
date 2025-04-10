package edu.ntnu.fullstack.amazoom.listing.repository


import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ListingRepository : JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing>{
    fun findListingBySellerId(sellerId: Long): List<Listing>

    @Query("SELECT l FROM Listing l JOIN l.bookmarks b WHERE b.user.id = :userId")
    fun findBookmarkedListingsByUserId(userId: Long, pageable: Pageable): Page<Listing>

}




