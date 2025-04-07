package edu.ntnu.fullstack.amazoom.bookmark.controller

import edu.ntnu.fullstack.amazoom.bookmark.service.ListingBookmarkService
import edu.ntnu.fullstack.amazoom.listing.dto.ListingResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookmarks")
class ListingBookmarkController(private val listingBookmarkService: ListingBookmarkService) {

    /**
     * Create a new bookmark for a listing.
     *
     * @param request the request containing the listing ID to bookmark
     * @return the created bookmark response
     */
    @PostMapping
    fun createBookmark(@RequestBody request: CreateOrUpdateListingBookmarkRequest): ResponseEntity<ListingBookmarkResponse> {
        val response = listingBookmarkService.createBookmark(request)
        return ResponseEntity.ok(response)
    }

    /**
     * Delete a bookmark by its ID.
     *
     * @param id the ID of the bookmark to delete
     * @return a response entity with no content
     */
    @DeleteMapping("/{id}")
    fun deleteBookmark(@PathVariable id: Long): ResponseEntity<Void> {
        listingBookmarkService.deleteBookmark(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * List all bookmarks for a user.
     *
     * @return a list of bookmark responses
     */
    @GetMapping
    fun listAllBookmarksForUser(): ResponseEntity<List<ListingBookmarkResponse>> {
        val responses = listingBookmarkService.listAllBookmarksForUser()
        return ResponseEntity.ok(responses)
    }
}



data class ListingBookmarkResponse(
    val id: Long,
    val listing: ListingResponse,
)

data class CreateOrUpdateListingBookmarkRequest(
    val listingId: Long,
)