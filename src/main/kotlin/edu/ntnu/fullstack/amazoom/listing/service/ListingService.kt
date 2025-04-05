package edu.ntnu.fullstack.amazoom.listing.service

import edu.ntnu.fullstack.amazoom.auth.repository.UserRepository
import edu.ntnu.fullstack.amazoom.category.exception.CategoryNotFoundException
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import edu.ntnu.fullstack.amazoom.common.service.CurrentUser
import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequest
import edu.ntnu.fullstack.amazoom.listing.dto.ListingResponse
import edu.ntnu.fullstack.amazoom.listing.mapper.ListingMapper
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class ListingService(
    private val listingRepository: ListingRepository,
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val currentUser: CurrentUser
) {

    fun createListing(request: CreateOrUpdateListingRequest): ListingResponse {
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { CategoryNotFoundException("No category found with ID=${request.categoryId}") }

        val sellerId = currentUser.getCurrentAuthenticatedUser()
            .orElseThrow { IllegalStateException("No authenticated user found") }
            .getSub()

        val seller = userRepository.findById(sellerId)
            .orElseThrow { NoSuchElementException("No user found with ID=$sellerId") }

        val entity = ListingMapper.toEntity(request, category, seller)
        val savedEntity = listingRepository.save(entity)
        return ListingMapper.toResponseDto(savedEntity)
    }

    fun getListing(id: Long): ListingResponse {
        val listing = listingRepository.findById(id)
            .orElseThrow { NoSuchElementException("Listing with id $id not found") }
        return ListingMapper.toResponseDto(listing)
    }

    @PreAuthorize("hasRole('ADMIN') or @listingService.isListingOwner(#id, authentication)")
    fun updateListing(id: Long, request: CreateOrUpdateListingRequest): ListingResponse {
        val existingListing = listingRepository.findById(id)
            .orElseThrow { NoSuchElementException("Listing with id $id not found") }
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { NoSuchElementException("No category found with ID=${request.categoryId}") }
        val updatedEntity = ListingMapper.updateEntity(existingListing, request, category)
        val savedEntity = listingRepository.save(updatedEntity)
        return ListingMapper.toResponseDto(savedEntity)
    }

    @PreAuthorize("hasRole('ADMIN') or @listingService.isListingOwner(#id, authentication)")
    fun deleteListing(id: Long) {
        if (!listingRepository.existsById(id)) {
            throw NoSuchElementException("Listing with id $id not found")
        }
        listingRepository.deleteById(id)
    }

    fun getPaginatedAndSortedListings(page: Int, size: Int, sortBy: String, direction: Sort.Direction): Page<ListingResponse> {
        val pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))
        val listingsPage = listingRepository.findAll(pageable)
        return listingsPage.map { ListingMapper.toResponseDto(it) }
    }

    fun isListingOwner(listingId: Long, userId: Long): Boolean {
        val listing = listingRepository.findById(listingId)
        return if (listing.isPresent) {
            val listingEntity = listing.get()
            listingEntity.seller.id == userId
        } else {
            false
        }
    }

}
