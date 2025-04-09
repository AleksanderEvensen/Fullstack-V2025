package edu.ntnu.fullstack.amazoom.bookmark.service

import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import edu.ntnu.fullstack.amazoom.bookmark.dto.CreateListingBookmarkRequestDto
import edu.ntnu.fullstack.amazoom.bookmark.dto.ListingBookmarkResponseDto
import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkOwnListingException
import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkAlreadyExistsException
import edu.ntnu.fullstack.amazoom.bookmark.mapper.ListingBookmarkMapper
import edu.ntnu.fullstack.amazoom.bookmark.repository.ListingBookmarkRepository
import edu.ntnu.fullstack.amazoom.common.exception.UnauthorizedException
import edu.ntnu.fullstack.amazoom.common.service.UserService
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.exception.ListingNotFoundException
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Service for managing listing bookmarks.
 * Handles creation and management of user bookmarks for listings.
 */
@Service
class ListingBookmarkService(
    private val listingBookmarkRepository: ListingBookmarkRepository,
    private val listingRepository: ListingRepository,
    private val userRepository: UserRepository,
    private val userService: UserService
)  {
    private val logger = LoggerFactory.getLogger(ListingBookmarkService::class.java)

    /**
     * Creates a new bookmark for a listing.
     *
     * @param request The request with the listing ID to bookmark
     * @return The created bookmark as a DTO
     * @throws ListingNotFoundException if the listing does not exist
     * @throws BookmarkAlreadyExistsException if the bookmark already exists
     * @throws BookmarkOwnListingException if trying to bookmark own listing
     */
    @Transactional
    fun createBookmark(request: CreateListingBookmarkRequestDto): ListingBookmarkResponseDto {
        val listing: Listing = listingRepository.findById(request.listingId)
            .orElseThrow { ListingNotFoundException("No listing found with id=${request.listingId}") }

        val currentUser = userService.getCurrentUser()

        if (listingBookmarkRepository.existsByUserIdAndListingId(currentUser.id, listing.id)) {
            logger.warn("Bookmark already exists for listing ID: {} and user ID: {}",
                listing.id, currentUser.id)
            throw BookmarkAlreadyExistsException()
        }

        if (listing.seller.id == currentUser.id) {
            logger.warn("User attempted to bookmark their own listing: {}", listing.id)
            throw BookmarkOwnListingException()
        }

        val bookmark: ListingBookmark = ListingBookmarkMapper.toEntity(listing, currentUser)
        val saved = listingBookmarkRepository.save(bookmark)

        logger.info("Created bookmark with ID: {} for listing: {}", saved.id, listing.id)
        return ListingBookmarkMapper.toResponseDto(saved)
    }

    /**
     * Deletes a bookmark by its ID.
     * Users can only delete their own bookmarks.
     *
     * @param id The ID of the bookmark to delete
     * @throws ListingNotFoundException if the bookmark does not exist
     * @throws UnauthorizedException if user is not authorized to delete the bookmark
     */
    @Transactional
    fun deleteBookmark(id: Long) {
        val currentUser = userService.getCurrentUser()

        val bookmark = listingBookmarkRepository.findById(id)
            .orElseThrow { ListingNotFoundException("No bookmark found with id=$id") }

        if (bookmark.user.id != currentUser.id) {
            logger.warn("User {} attempted to delete bookmark {} belonging to user {}",
                currentUser.id, id, bookmark.user.id)
            throw UnauthorizedException("User is not authorized to delete this bookmark")
        }

        listingBookmarkRepository.deleteById(id)
        logger.info("Deleted bookmark with ID: {}", id)
    }

    /**
     * Lists all bookmarks for the current user.
     *
     * @return A list of all the user's bookmarks
     */
    fun listAllBookmarksForUser(): List<ListingBookmarkResponseDto> {
        val currentUser = userService.getCurrentUser()
        logger.debug("Getting all bookmarks for user with ID: {}", currentUser.id)

        val allBookmarks = listingBookmarkRepository.findAllForUser(currentUser.id)
        return allBookmarks.map { ListingBookmarkMapper.toResponseDto(it) }
    }
}