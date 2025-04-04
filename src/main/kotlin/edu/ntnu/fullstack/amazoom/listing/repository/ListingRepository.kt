package edu.ntnu.fullstack.amazoom.listing.repository


import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ListingRepository : JpaRepository<Listing, Long>
