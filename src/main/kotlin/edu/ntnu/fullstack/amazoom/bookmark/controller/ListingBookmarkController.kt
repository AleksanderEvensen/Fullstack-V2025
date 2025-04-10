package edu.ntnu.fullstack.amazoom.bookmark.controller

import edu.ntnu.fullstack.amazoom.bookmark.dto.CreateListingBookmarkRequestDto
import edu.ntnu.fullstack.amazoom.bookmark.dto.ListingBookmarkResponseDto
import edu.ntnu.fullstack.amazoom.bookmark.service.ListingBookmarkService
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing listing bookmarks.
 * Provides endpoints for creating, deleting, and listing bookmarks.
 */
@RestController
@RequestMapping("/api/bookmarks")
@Tag(name = "Bookmarks", description = "Operations for managing listing bookmarks")
class ListingBookmarkController(private val listingBookmarkService: ListingBookmarkService) {
    private val logger = LoggerFactory.getLogger(ListingBookmarkController::class.java)

    /**
     * Creates a new bookmark for a listing.
     *
     * @param listingId The ID of the listing to bookmark
     * @return The created bookmark
     */
    @Operation(
        summary = "Create a bookmark",
        description = "Creates a new bookmark for a listing. Users cannot bookmark their own listings."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "Bookmark created successfully",
            content = [Content(schema = Schema(implementation = ListingBookmarkResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Cannot bookmark own listing",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Listing not found",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "409",
            description = "Bookmark already exists",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
    @PostMapping("/{listingId}")
    fun createBookmark(@PathVariable listingId: Long): ResponseEntity<ListingBookmarkResponseDto> {
        logger.debug("REST request to create bookmark for listing: {}", listingId)

        val response = listingBookmarkService.createBookmarkByListingId(listingId)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    /**
     * Deletes a bookmark by its listing ID.
     *
     * @param listingId The ID of the listing to remove from bookmarks
     * @return A no content response
     */
    @Operation(
        summary = "Delete a bookmark",
        description = "Deletes a bookmark by its listing ID. Users can only delete their own bookmarks."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "204",
            description = "Bookmark deleted successfully"
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized to delete this bookmark",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Bookmark not found",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
    @DeleteMapping("/{listingId}")
    fun deleteBookmark(@PathVariable listingId: Long): ResponseEntity<Void> {
        logger.debug("REST request to delete bookmark for listing: {}", listingId)
        listingBookmarkService.deleteBookmarkByListingId(listingId)
        return ResponseEntity.noContent().build()
    }

    /**
     * Lists all bookmarks for the current user.
     *
     * @return A list of the user's bookmarks
     */
    @Operation(
        summary = "List user bookmarks",
        description = "Lists all bookmarks for the currently authenticated user"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "List of bookmarks retrieved successfully",
            content = [Content(schema = Schema(implementation = ListingBookmarkResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
    @GetMapping
    fun listAllBookmarksForUser(): ResponseEntity<List<ListingBookmarkResponseDto>> {
        logger.debug("REST request to get all bookmarks for current user")

        val responses = listingBookmarkService.listAllBookmarksForUser()
        return ResponseEntity.ok(responses)
    }
}