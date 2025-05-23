package edu.ntnu.fullstack.amazoom.listing.service

import edu.ntnu.fullstack.amazoom.bookmark.repository.ListingBookmarkRepository
import edu.ntnu.fullstack.amazoom.category.exception.CategoryNotFoundException
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import edu.ntnu.fullstack.amazoom.common.service.UserService
import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequestDto
import edu.ntnu.fullstack.amazoom.listing.dto.ListingDto
import edu.ntnu.fullstack.amazoom.listing.dto.ListingSearchRequestDto
import edu.ntnu.fullstack.amazoom.listing.dto.MyListingRequest
import edu.ntnu.fullstack.amazoom.listing.entity.ListingStatus
import edu.ntnu.fullstack.amazoom.listing.exception.ListingNotFoundException
import edu.ntnu.fullstack.amazoom.listing.mapper.ListingMapper
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import edu.ntnu.fullstack.amazoom.listing.repository.ListingSpecification
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

/**
 * Service for managing product listings.
 * Handles CRUD operations for listings.
 */
@Service
class ListingService(
    private val listingRepository: ListingRepository,
    private val categoryRepository: CategoryRepository,
    private val bookmarksRepository: ListingBookmarkRepository,
    private val userService: UserService
) {
    private val logger = LoggerFactory.getLogger(ListingService::class.java)

    /**
     * Creates a new listing.
     *
     * @param request The request DTO with listing details
     * @return The created listing as a DTO
     * @throws CategoryNotFoundException if the specified category does not exist
     */
    @Transactional
    fun createListing(request: CreateOrUpdateListingRequestDto): ListingDto {
        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { CategoryNotFoundException("No category found with ID=${request.categoryId}") }

        val seller = userService.getCurrentUser()

        val entity = ListingMapper.toEntity(request, category, seller)
        val savedEntity = listingRepository.save(entity)

        logger.info("Created listing with ID: {}", savedEntity.id)
        return ListingMapper.toResponseDto(savedEntity)
    }

    /**
     * Retrieves a listing by its ID.
     *
     * @param id The ID of the listing to retrieve
     * @return The listing entity
     * @throws ListingNotFoundException if the listing does not exist
     */
    fun getListing(id: Long): ListingDto {
        val listing = listingRepository.findById(id)
            .orElseThrow { ListingNotFoundException("Listing with id $id not found") }

        logger.info("Retrieved listing with ID: {}", id)
        val currentUser = try {
            userService.getCurrentUser()
        } catch (e: Exception) {
            null
        }

        val listingDto = ListingMapper.toResponseDto(listing)
        if (currentUser != null) {
            listingDto.isBookmarked =
                bookmarksRepository.existsByUserIdAndListingId(currentUser.id, listing.id)
        }
        return listingDto

    }

    /**
     * Updates an existing listing.
     *
     * @param id The ID of the listing to update
     * @param request The request DTO with updated details
     * @return The updated listing as a DTO
     * @throws ListingNotFoundException if the listing does not exist
     * @throws CategoryNotFoundException if the specified category does not exist
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @listingService.isListingOwner(#id, authentication.name)")
    fun updateListing(id: Long, request: CreateOrUpdateListingRequestDto): ListingDto {

        val existingListing = listingRepository.findById(id)
            .orElseThrow { ListingNotFoundException("Listing with id $id not found") }

        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { CategoryNotFoundException("No category found with ID=${request.categoryId}") }

        val updatedEntity = ListingMapper.updateEntity(existingListing, request, category)
        val savedEntity = listingRepository.save(updatedEntity)

        logger.info("Updated listing with ID: {}", savedEntity.id)
        return ListingMapper.toResponseDto(savedEntity)
    }

    /**
     * Deletes a listing by its ID.
     *
     * @param id The ID of the listing to delete
     * @throws ListingNotFoundException if the listing does not exist
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @listingService.isListingOwner(#id, authentication.name)")
    fun deleteListing(id: Long) {

        if (!listingRepository.existsById(id)) {
            throw ListingNotFoundException("Listing with id $id not found")
        }

        listingRepository.deleteById(id)
        logger.info("Deleted listing with ID: {}", id)
    }

    /**
     * Retrieves a paginated and sorted list of listings.
     *
     * @param page The page number to retrieve (0-based)
     * @param size The number of listings per page
     * @param sortBy The field to sort by
     * @param direction The sort direction (ASC or DESC)
     * @param status The status of the listings to retrieve
     * @return A page of listing DTOs
     */
    fun getPaginatedAndSortedListings(
        page: Int,
        size: Int,
        sortBy: String,
        direction: Sort.Direction,
        status: ListingStatus
    ): Page<ListingDto> {
        val pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))
        val listingSpecification = ListingSpecification.buildSpecification(
            status = status
        )
        val listingsPage = listingRepository.findAll(listingSpecification, pageable)
        val currentUser = try {
            userService.getCurrentUser()
        } catch (e: Exception) {
            null
        }
        return listingsPage.map {
            val listingDto = ListingMapper.toResponseDto(it)
            if (currentUser != null) {
                listingDto.isBookmarked =
                    bookmarksRepository.existsByUserIdAndListingId(currentUser.id, it.id)
            }
            listingDto
        }
    }

    /**
     * Checks if the specified user is the owner of a listing.
     *
     * @param listingId The ID of the listing to check
     * @param userEmail The email of the user to check
     * @return True if the user is the owner, false otherwise
     */
    fun isListingOwner(listingId: Long, userEmail: String): Boolean {
        val listing = listingRepository.findById(listingId)
        return if (listing.isPresent) {
            val listingEntity = listing.get()
            listingEntity.seller.email == userEmail
        } else {
            false
        }
    }


    /**
     * Retrieves a list of listings created by the current user.
     *
     * @param myListingRequest The request DTO containing pagination and sorting details
     * @return A list of listing DTOs
     */
    fun getMyListings(myListingRequest: MyListingRequest): Page<ListingDto> {
        val currentUser = userService.getCurrentUser()
        val pageable = PageRequest.of(myListingRequest.page, myListingRequest.size, Sort.by(myListingRequest.sortBy))
        val listingSpecification = ListingSpecification.buildSpecification(
            sellerId = currentUser.id,
            status = myListingRequest.status
        )
        val listings = listingRepository.findAll(listingSpecification, pageable)
        return listings.map {
            ListingMapper.toResponseDto(it)
        }
    }

    /**
     * Retrieves a list of bookmarked listings for the current user.
     *
     * @param myListingRequest The request DTO containing pagination and sorting details
     * @return A list of listing DTOs
     */
    fun getBookmarkedListings(myListingRequest: MyListingRequest): Page<ListingDto> {
        val currentUser = userService.getCurrentUser()
        val pageable = PageRequest.of(myListingRequest.page, myListingRequest.size, Sort.by(myListingRequest.sortBy))
        val listingPage = listingRepository.findBookmarkedListingsByUserId(
            userId = currentUser.id,
            pageable = pageable
        )
        return listingPage.map {
            ListingMapper.toResponseDto(it)
        }
    }

    /**
     * Searches for listings based on various criteria.
     *
     * @param searchRequest The request DTO containing search criteria
     */
    fun searchListings(searchRequest: ListingSearchRequestDto): Page<ListingDto> {
        val direction = Sort.Direction.valueOf(searchRequest.sortDirection)
        val pageable = PageRequest.of(
            searchRequest.page,
            searchRequest.size,
            Sort.by(direction, searchRequest.sortBy)
        )

        val specification = ListingSpecification.buildSpecification(
            q = searchRequest.q,
            categoryId = searchRequest.categoryId,
            categoryName = searchRequest.categoryName,
            condition = searchRequest.condition,
            minPrice = searchRequest.minPrice,
            maxPrice = searchRequest.maxPrice,
            minModelYear = searchRequest.minModelYear,
            maxModelYear = searchRequest.maxModelYear,
            manufacturer = searchRequest.manufacturer,
            model = searchRequest.model,
            sellerId = searchRequest.sellerId,
            defectsCount = searchRequest.defectsCount,
            modificationsCount = searchRequest.modificationsCount,
            latitude = searchRequest.latitude,
            longitude = searchRequest.longitude,
            radiusKm = searchRequest.radiusKm,
        )

        val listingsPage = listingRepository.findAll(specification, pageable)
        val currentUser = try {
            userService.getCurrentUser()
        } catch (e: Exception) {
            null
        }
        return listingsPage.map {
            val listingDto = ListingMapper.toResponseDto(it)
            if (currentUser != null) {
                listingDto.isBookmarked =
                    bookmarksRepository.existsByUserIdAndListingId(currentUser.id, it.id)
            }
            listingDto
        }
    }
}