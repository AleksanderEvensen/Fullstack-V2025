package edu.ntnu.fullstack.amazoom.listing.controller

import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequest
import edu.ntnu.fullstack.amazoom.listing.dto.ListingResponse
import edu.ntnu.fullstack.amazoom.listing.service.ListingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing listings.
 */
@RestController
@RequestMapping("/api/listings")
class ListingController(
    private val listingService: ListingService
) {

    /**
     * Creates a new listing.
     *
     * @param request the listing request
     * @return the created listing response
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createListing(@RequestBody request: CreateOrUpdateListingRequest): ListingResponse {
        return listingService.createListing(request)
    }

    /**
     * Retrieves a listing by its ID.
     *
     * @param id the ID of the listing
     * @return the listing response
     */
    @GetMapping("/{id}")
    fun getListing(@PathVariable id: Long): ListingResponse {
        return listingService.getListing(id)
    }

    /**
     * Updates an existing listing.
     *
     * @param id the ID of the listing to update
     * @param request the listing request with updated data
     * @return the updated listing response
     */
    @PutMapping("/{id}")
    fun updateListing(
        @PathVariable id: Long,
        @RequestBody request: CreateOrUpdateListingRequest
    ): ListingResponse {
        return listingService.updateListing(id, request)
    }

    /**
     * Deletes a listing by its ID.
     *
     * @param id the ID of the listing to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteListing(@PathVariable id: Long) {
        listingService.deleteListing(id)
    }

    /**
     * Lists all listings.
     *
     * @return a list of all listing responses
     */
    @GetMapping
    fun listAll(): List<ListingResponse> {
        return listingService.listAllListings()
    }
}