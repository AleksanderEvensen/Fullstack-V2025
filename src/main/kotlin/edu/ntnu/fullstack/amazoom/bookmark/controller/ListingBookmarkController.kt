package edu.ntnu.fullstack.amazoom.bookmark.controller

import edu.ntnu.fullstack.amazoom.bookmark.dto.CreateListingBookmarkRequestDto
import edu.ntnu.fullstack.amazoom.bookmark.dto.ListingBookmarkResponseDto
import edu.ntnu.fullstack.amazoom.bookmark.service.ListingBookmarkService
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
class ListingBookmarkController(private val listingBookmarkService: ListingBookmarkService) {
    private val logger = LoggerFactory.getLogger(ListingBookmarkController::class.java)

    /**
     * Creates a new bookmark for a listing.
     *
     * @param request The request containing the listing ID to bookmark
     * @return The created bookmark
     */
    @PostMapping
    fun createBookmark(@Valid @RequestBody request: CreateListingBookmarkRequestDto): ResponseEntity<ListingBookmarkResponseDto> {
        logger.debug("REST request to create bookmark for listing: {}", request.listingId)

        val response = listingBookmarkService.createBookmark(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    /**
     * Deletes a bookmark by its ID.
     *
     * @param id The ID of the bookmark to delete
     * @return A no content response
     */
    @DeleteMapping("/{id}")
    fun deleteBookmark(@PathVariable id: Long): ResponseEntity<Void> {
        logger.debug("REST request to delete bookmark: {}", id)

        listingBookmarkService.deleteBookmark(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Lists all bookmarks for the current user.
     *
     * @return A list of the user's bookmarks
     */
    @GetMapping
    fun listAllBookmarksForUser(): ResponseEntity<List<ListingBookmarkResponseDto>> {
        logger.debug("REST request to get all bookmarks for current user")

        val responses = listingBookmarkService.listAllBookmarksForUser()
        return ResponseEntity.ok(responses)
    }
}