package edu.ntnu.fullstack.amazoom.listing.controller

import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequest
import edu.ntnu.fullstack.amazoom.listing.dto.ListingDto
import edu.ntnu.fullstack.amazoom.listing.dto.ListingSearchRequest
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.service.ListingService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun createListing(@RequestBody request: CreateOrUpdateListingRequest): ListingDto {
        return listingService.createListing(request)
    }

    /**
     * Retrieves a listing by its ID.
     *
     * @param id the ID of the listing
     * @return the listing response
     */
    @GetMapping("/{id}")
    fun getListing(@PathVariable id: Long): ListingDto {
        return listingService.getListing(id).toDto()
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
    ): ListingDto {
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
    fun getPaginatedAndSortedListings(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "price") sortBy: String,
        @RequestParam(defaultValue = "ASC") direction: Sort.Direction
    ): ResponseEntity<Page<ListingDto>> {
        val paginatedListings = listingService.getPaginatedAndSortedListings(page, size, sortBy, direction)
        return ResponseEntity.ok(paginatedListings)
    }
    
    /**
     * Search for listings with advanced filters
     *
     * @return page of filtered listing responses
     */
    @GetMapping("/search")
    fun searchListings(
        @RequestParam(required = false) q: String?,
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) description: String?,
        @RequestParam(required = false) categoryId: Long?,
        @RequestParam(required = false) condition: ListingCondition?,
        @RequestParam(required = false) minPrice: Double?,
        @RequestParam(required = false) maxPrice: Double?,
        @RequestParam(required = false) minModelYear: Int?,
        @RequestParam(required = false) maxModelYear: Int?,
        @RequestParam(required = false) manufacturer: String?,
        @RequestParam(required = false) model: String?,
        @RequestParam(required = false) sellerId: Long?,
        @RequestParam(required = false) defectsCount: Int?,
        @RequestParam(required = false) modificationsCount: Int?,
        @RequestParam(required = false) categoryName: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "DESC") sortDirection: String
    ): ResponseEntity<Page<ListingDto>> {
        val searchRequest = ListingSearchRequest(
            q = q,
            categoryId = categoryId,
            condition = condition,
            minPrice = minPrice,
            maxPrice = maxPrice,
            minModelYear = minModelYear,
            maxModelYear = maxModelYear,
            manufacturer = manufacturer,
            categoryName = categoryName,
            model = model,
            sellerId = sellerId,
            defectsCount = defectsCount,
            modificationsCount = modificationsCount,
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection
        )
        
        val searchResults = listingService.searchListings(searchRequest)
        return ResponseEntity.ok(searchResults)
    }
    
    /**
     * API endpoint for advanced search using a request body
     */
    @PostMapping("/search")
    fun advancedSearchListings(@RequestBody searchRequest: ListingSearchRequest): ResponseEntity<Page<ListingDto>> {
        val searchResults = listingService.searchListings(searchRequest)
        return ResponseEntity.ok(searchResults)
    }
}