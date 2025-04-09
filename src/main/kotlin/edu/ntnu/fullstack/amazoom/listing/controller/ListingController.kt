package edu.ntnu.fullstack.amazoom.listing.controller

import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequestDto
import edu.ntnu.fullstack.amazoom.listing.dto.ListingDto
import edu.ntnu.fullstack.amazoom.listing.service.ListingService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing product listings.
 * Provides endpoints for CRUD operations on listings.
 */
@RestController
@RequestMapping("/api/listings")
class ListingController(
    private val listingService: ListingService
) {
    private val logger = LoggerFactory.getLogger(ListingController::class.java)

    /**
     * Creates a new listing.
     *
     * @param request The listing request with details
     * @return The created listing
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createListing(@Valid @RequestBody request: CreateOrUpdateListingRequestDto): ResponseEntity<ListingDto> {
        val createdListing = listingService.createListing(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdListing)
    }

    /**
     * Retrieves a listing by its ID.
     *
     * @param id The ID of the listing to retrieve
     * @return The listing
     */
    @GetMapping("/{id}")
    fun getListing(@PathVariable id: Long): ResponseEntity<ListingDto> {
        logger.debug("REST request to get listing: {}", id)
        val listing = listingService.getListing(id)
        return ResponseEntity.ok(listing)
    }

    /**
     * Updates an existing listing.
     *
     * @param id The ID of the listing to update
     * @param request The listing request with updated data
     * @return The updated listing
     */
    @PutMapping("/{id}")
    fun updateListing(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateOrUpdateListingRequestDto
    ): ResponseEntity<ListingDto> {
        logger.debug("REST request to update listing: {}", id)
        val updatedListing = listingService.updateListing(id, request)
        return ResponseEntity.ok(updatedListing)
    }

    /**
     * Deletes a listing by its ID.
     *
     * @param id The ID of the listing to delete
     * @return No content response
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteListing(@PathVariable id: Long): ResponseEntity<Void> {
        logger.debug("REST request to delete listing: {}", id)
        listingService.deleteListing(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Lists all listings with pagination and sorting.
     *
     * @param page The page number (0-based)
     * @param size The page size
     * @param sortBy The field to sort by
     * @param direction The sort direction
     * @return A page of listings
     */
    @GetMapping
    fun getPaginatedAndSortedListings(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "price") sortBy: String,
        @RequestParam(defaultValue = "ASC") direction: Sort.Direction
    ): ResponseEntity<Page<ListingDto>> {
        logger.debug("REST request to get listings page: {}, size: {}", page, size)
        val paginatedListings = listingService.getPaginatedAndSortedListings(page, size, sortBy, direction)
        return ResponseEntity.ok(paginatedListings)
    }
}