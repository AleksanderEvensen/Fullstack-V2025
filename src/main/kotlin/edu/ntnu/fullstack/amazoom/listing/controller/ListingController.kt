package edu.ntnu.fullstack.amazoom.listing.controller

import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequestDto
import edu.ntnu.fullstack.amazoom.listing.dto.ListingDto
import edu.ntnu.fullstack.amazoom.listing.dto.ListingSearchRequestDto
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.service.ListingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Listings", description = "Operations for managing product listings")
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
    @Operation(
        summary = "Create a listing",
        description = "Creates a new product listing for the currently authenticated user"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "Listing created successfully",
            content = [Content(schema = Schema(implementation = ListingDto::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Get a listing by ID",
        description = "Retrieves a product listing by its unique identifier"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Listing found",
            content = [Content(schema = Schema(implementation = ListingDto::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Listing not found",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Update a listing",
        description = "Updates an existing product listing. User must be the owner of the listing or an admin."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Listing updated successfully",
            content = [Content(schema = Schema(implementation = ListingDto::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "403",
            description = "User not authorized to update this listing",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Listing or category not found",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Delete a listing",
        description = "Deletes a product listing by its ID. User must be the owner of the listing or an admin."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "204",
            description = "Listing deleted successfully"
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "403",
            description = "User not authorized to delete this listing",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Listing not found",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Get paginated and sorted listings",
        description = "Retrieves a paginated and sorted list of all listings"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Listings retrieved successfully",
        )
    )
    @GetMapping
    fun getPaginatedAndSortedListings(
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Number of items per page", example = "10")
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "Field to sort by", example = "price")
        @RequestParam(defaultValue = "price") sortBy: String,
        @Parameter(description = "Sort direction", example = "ASC")
        @RequestParam(defaultValue = "ASC") direction: Sort.Direction
    ): ResponseEntity<Page<ListingDto>> {
        logger.debug("REST request to get listings page: {}, size: {}", page, size)
        val paginatedListings = listingService.getPaginatedAndSortedListings(page, size, sortBy, direction)
        return ResponseEntity.ok(paginatedListings)
    }
    
    /**
     * Search for listings with advanced filters
     *
     * @return page of filtered listing responses
     */
    @Operation(
        summary = "Search listings",
        description = "Searches for listings with advanced filters"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Search results retrieved successfully",
        )
    )
    @GetMapping("/search")
    fun searchListings(
        @Parameter(description = "Search query for title and description", example = "Macbook")
        @RequestParam(required = false) q: String?,
        @Parameter(description = "Title filter", example = "Macbook Pro")
        @RequestParam(required = false) title: String?,
        @Parameter(description = "Description filter", example = "Excellent condition")
        @RequestParam(required = false) description: String?,
        @Parameter(description = "Category ID filter", example = "1")
        @RequestParam(required = false) categoryId: Long?,
        @Parameter(description = "Condition filter", schema = Schema(type = "string", allowableValues = ["NEW", "LIKE_NEW", "VERY_GOOD", "GOOD", "ACCEPTABLE"]))
        @RequestParam(required = false) condition: ListingCondition?,
        @Parameter(description = "Minimum price filter", example = "500.0")
        @RequestParam(required = false) minPrice: Double?,
        @Parameter(description = "Maximum price filter", example = "2000.0")
        @RequestParam(required = false) maxPrice: Double?,
        @Parameter(description = "Minimum model year filter", example = "2020")
        @RequestParam(required = false) minModelYear: Int?,
        @Parameter(description = "Maximum model year filter", example = "2023")
        @RequestParam(required = false) maxModelYear: Int?,
        @Parameter(description = "Manufacturer filter", example = "Apple")
        @RequestParam(required = false) manufacturer: String?,
        @Parameter(description = "Model filter", example = "Pro")
        @RequestParam(required = false) model: String?,
        @Parameter(description = "Seller ID filter", example = "1")
        @RequestParam(required = false) sellerId: Long?,
        @Parameter(description = "Defects count filter", example = "0")
        @RequestParam(required = false) defectsCount: Int?,
        @Parameter(description = "Modifications count filter", example = "1")
        @RequestParam(required = false) modificationsCount: Int?,
        @Parameter(description = "Category name filter", example = "Electronics")
        @RequestParam(required = false) categoryName: String?,
        @Parameter(description = "Page number (0-based)", example = "0")
        @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Number of items per page", example = "20")
        @RequestParam(defaultValue = "20") size: Int,
        @Parameter(description = "Field to sort by", example = "createdAt", schema = Schema(type = "string"))
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @Parameter(description = "Sort direction", example = "DESC", schema = Schema(type = "string"))
        @RequestParam(defaultValue = "DESC") sortDirection: String
    ): ResponseEntity<Page<ListingDto>> {
        val searchRequest = ListingSearchRequestDto(
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
    @Operation(
        summary = "Advanced search listings",
        description = "Advanced search for listings using request body for complex filters"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Advanced search results retrieved successfully",
        )
    )
    @PostMapping("/search")
    fun advancedSearchListings(@RequestBody searchRequest: ListingSearchRequestDto): ResponseEntity<Page<ListingDto>> {
        val searchResults = listingService.searchListings(searchRequest)
        return ResponseEntity.ok(searchResults)
    }
}