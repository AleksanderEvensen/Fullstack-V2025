package edu.ntnu.fullstack.amazoom.bookmark.service

import edu.ntnu.fullstack.amazoom.auth.entity.User
import edu.ntnu.fullstack.amazoom.auth.repository.UserRepository
import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkOwnListingException
import edu.ntnu.fullstack.amazoom.bookmark.dto.CreateOrUpdateListingBookmarkRequest
import edu.ntnu.fullstack.amazoom.bookmark.dto.ListingBookmarkResponse
import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkAlreadyExists
import edu.ntnu.fullstack.amazoom.bookmark.mapper.ListingBookmarkMapper
import edu.ntnu.fullstack.amazoom.bookmark.repository.ListingBookmarkRepository
import edu.ntnu.fullstack.amazoom.common.service.CurrentUser
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.exception.ListingNotFoundException
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ListingBookmarkService(
    private val listingBookmarkRepository: ListingBookmarkRepository,
    private val listingRepository: ListingRepository,
    private val userRepository: UserRepository,
    private val currentUser: CurrentUser
)  {

    fun createBookmark(request: CreateOrUpdateListingBookmarkRequest): ListingBookmarkResponse {
        val listing: Listing = listingRepository.findById(request.listingId)
            .orElseThrow { ListingNotFoundException("No listing found with id=${request.listingId}") }

        val authenticatedUser = currentUser.getCurrentAuthenticatedUser().getOrNull()
            ?: throw IllegalStateException("No authenticated user found")

        val user: User = userRepository.findById(authenticatedUser.getSub())
            .orElseThrow { NoSuchElementException("No user found with id=${authenticatedUser.getSub()}") }

        if (listingBookmarkRepository.existsByUserIdAndListingId(user.id, listing.id)) {
            throw BookmarkAlreadyExists()
        }

        if (listing.seller.id == user.id) {
            throw BookmarkOwnListingException()
        }

        val bookmark: ListingBookmark = ListingBookmarkMapper.toEntity(listing, user)
        val saved = listingBookmarkRepository.save(bookmark)
        return ListingBookmarkMapper.toResponse(saved)
    }


    @PreAuthorize("listingBookmarkRepository.existsByUserIdAndListingId(#currentUser.currentAuthenticatedUser.get().sub, #id)")
    fun deleteBookmark(id: Long) {
        if (!listingBookmarkRepository.existsById(id)) {
            throw NoSuchElementException("No bookmark found with id=$id")
        }
        listingBookmarkRepository.deleteById(id)
    }

    fun listAllBookmarksForUser(): List<ListingBookmarkResponse> {
        val authenticatedUser = currentUser.getCurrentAuthenticatedUser().getOrNull()
            ?: throw IllegalStateException("No authenticated user found")

        val allBookmarks = listingBookmarkRepository.findAll()
            .filter { it.user.id == authenticatedUser.getSub() }

        return allBookmarks.map { ListingBookmarkMapper.toResponse(it) }
    }
}