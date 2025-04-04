package edu.ntnu.fullstack.amazoom.listing.service

import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequest
import edu.ntnu.fullstack.amazoom.listing.dto.ListingResponse
import edu.ntnu.fullstack.amazoom.listing.mapper.ListingMapper
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import org.springframework.stereotype.Service

@Service
class ListingService(
    private val listingRepository: ListingRepository,
    private val categoryRepository: CategoryRepository
) {

    fun createListing(request: CreateOrUpdateListingRequest): ListingResponse {
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { NoSuchElementException("No category found with ID=${request.categoryId}") }
        val entity = ListingMapper.toEntity(request, category)
        val savedEntity = listingRepository.save(entity)
        return ListingMapper.toResponseDto(savedEntity)
    }

    fun getListing(id: Long): ListingResponse {
        val listing = listingRepository.findById(id)
            .orElseThrow { NoSuchElementException("Listing with id $id not found") }
        return ListingMapper.toResponseDto(listing)
    }

    fun updateListing(id: Long, request: CreateOrUpdateListingRequest): ListingResponse {
        val existingListing = listingRepository.findById(id)
            .orElseThrow { NoSuchElementException("Listing with id $id not found") }
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { NoSuchElementException("No category found with ID=${request.categoryId}") }
        val updatedEntity = ListingMapper.updateEntity(existingListing, request, category)
        val savedEntity = listingRepository.save(updatedEntity)
        return ListingMapper.toResponseDto(savedEntity)
    }

    fun deleteListing(id: Long) {
        if (!listingRepository.existsById(id)) {
            throw NoSuchElementException("Listing with id $id not found")
        }
        listingRepository.deleteById(id)
    }

    fun listAllListings(): List<ListingResponse> {
        return listingRepository.findAll()
            .map { ListingMapper.toResponseDto(it) }
    }
}
