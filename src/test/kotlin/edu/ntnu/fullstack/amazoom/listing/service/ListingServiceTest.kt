package edu.ntnu.fullstack.amazoom.listing.service

import edu.ntnu.fullstack.amazoom.bookmark.repository.ListingBookmarkRepository
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.category.exception.CategoryNotFoundException
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import edu.ntnu.fullstack.amazoom.common.entity.Address
import edu.ntnu.fullstack.amazoom.common.entity.Role
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.service.UserService
import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequestDto
import edu.ntnu.fullstack.amazoom.listing.dto.ListingDto
import edu.ntnu.fullstack.amazoom.listing.dto.ListingSearchRequestDto
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.entity.ListingStatus
import edu.ntnu.fullstack.amazoom.listing.exception.ListingNotFoundException
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import java.util.*

@ExtendWith(MockitoExtension::class)
class ListingServiceTest {
    @Mock
    private lateinit var listingRepository: ListingRepository

    @Mock
    private lateinit var categoryRepository: CategoryRepository

    @Mock
    private lateinit var bookmarksRepository: ListingBookmarkRepository

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var service: ListingService


    private val testUser = User(
        id = 1L,
        name = "Test User",
        email = "test@example.com",
        phoneNumber = "1234567890",
        password = "password",
        address = Address(
            streetName = "Test Street",
            streetNumber = "123",
            postalCode = "12345",
            city = "Test City",
            country = "Test Country"
        ),
        roles = mutableSetOf(Role(id = 1, name = RoleName.ROLE_USER))
    )

    private val testCategory = Category(
        id = 1L,
        name = "Electronics",
        description = "Electronic devices and gadgets",
        translationString = "electronics",
        icon = "electronics-icon"
    )

    private val testListing = Listing(
        id = 1L,
        title = "Test Listing",
        description = "Test Description",
        price = 100.0,
        seller = testUser,
        category = testCategory,
        condition = ListingCondition.NEW
    )

    @BeforeEach
    fun setup() {
    }

    @Test
    fun `test create listing successfully`() {
        // Given
        val request = CreateOrUpdateListingRequestDto(
            title = "New Listing",
            categoryId = 1L,
            condition = ListingCondition.NEW,
            price = 100.0,
            description = "A new listing description"
        )
        `when`(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory))
        `when`(listingRepository.save(any())).thenReturn(testListing)
        `when`(userService.getCurrentUser()).thenReturn(testUser)

        // When
        val result = service.createListing(request)

        // Then
        verify(categoryRepository).findById(1L)
        verify(userService).getCurrentUser()
        verify(listingRepository).save(any())

    }

    @Test
    fun `test create listing throws CategoryNotFoundException`() {
        // Given
        val request = CreateOrUpdateListingRequestDto(
            title = "New Listing",
            categoryId = 1L,
            condition = ListingCondition.NEW,
            price = 100.0,
            description = "A new listing description"
        )
        `when`(categoryRepository.findById(1L)).thenReturn(Optional.empty())

        // When/Then
        assertThrows<CategoryNotFoundException> {
            service.createListing(request)
        }
    }

    @Test
    fun `test get listing successfully`() {
        // Given
        `when`(listingRepository.findById(1L)).thenReturn(Optional.of(testListing))

        // When
        val result = service.getListing(1L)

        // Then
        assertEquals("Test Listing", result.title)
        verify(listingRepository).findById(1L)
    }

    @Test
    fun `test get listing throws ListingNotFoundException`() {
        // Given
        `when`(listingRepository.findById(1L)).thenReturn(Optional.empty())

        // When/Then
        assertThrows<ListingNotFoundException> {
            service.getListing(1L)
        }
    }

    @Test
    fun `test update listing successfully`() {
        // Given
        val request = CreateOrUpdateListingRequestDto(
            title = "Updated Listing",
            categoryId = 1L,
            condition = ListingCondition.GOOD,
            price = 150.0,
            description = "Updated description"
        )
        `when`(listingRepository.findById(1L)).thenReturn(Optional.of(testListing))
        `when`(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory))
        
        val updatedListing = testListing.copy(
            title = "Updated Listing",
            condition = ListingCondition.GOOD,
            price = 150.0,
            description = "Updated description"
        )
        `when`(listingRepository.save(any())).thenReturn(updatedListing)

        // When
        val result = service.updateListing(1L, request)


        assertEquals("Updated Listing", result.title)
        assertEquals(150.0, result.price)
        verify(listingRepository).findById(1L)
        verify(categoryRepository).findById(1L)
        verify(listingRepository).save(any())
    }

    @Test
    fun `test update listing throws ListingNotFoundException`() {
        // Given
        val request = CreateOrUpdateListingRequestDto(
            title = "Updated Listing",
            categoryId = 1L,
            condition = ListingCondition.GOOD,
            price = 150.0,
            description = "Updated description"
        )
        `when`(listingRepository.findById(1L)).thenReturn(Optional.empty())

        // When/Then
        assertThrows<ListingNotFoundException> {
            service.updateListing(1L, request)
        }
    }

    @Test
    fun `test update listing throws CategoryNotFoundException`() {
        // Given
        val request = CreateOrUpdateListingRequestDto(
            title = "Updated Listing",
            categoryId = 1L,
            condition = ListingCondition.GOOD,
            price = 150.0,
            description = "Updated description"
        )
        `when`(listingRepository.findById(1L)).thenReturn(Optional.of(testListing))
        `when`(categoryRepository.findById(1L)).thenReturn(Optional.empty())

        // When/Then
        assertThrows<CategoryNotFoundException> {
            service.updateListing(1L, request)
        }
    }

    @Test
    fun `test delete listing successfully`() {
        // Given
        `when`(listingRepository.existsById(1L)).thenReturn(true)
        doNothing().`when`(listingRepository).deleteById(1L)

        // When
        service.deleteListing(1L)

        // Then
        verify(listingRepository).existsById(1L)
        verify(listingRepository).deleteById(1L)
    }

    @Test
    fun `test delete listing throws ListingNotFoundException`() {
        // Given
        `when`(listingRepository.existsById(1L)).thenReturn(false)

        // When/Then
        assertThrows<ListingNotFoundException> {
            service.deleteListing(1L)
        }
    }



    @Test
    fun `test isListingOwner returns true`() {
        // Given
        `when`(listingRepository.findById(1L)).thenReturn(Optional.of(testListing))

        // When
        val result = service.isListingOwner(1L, "test@example.com")

        // Then
        assertTrue(result)
        verify(listingRepository).findById(1L)
    }

    @Test
    fun `test isListingOwner returns false when user is not owner`() {
        // Given
        `when`(listingRepository.findById(1L)).thenReturn(Optional.of(testListing))

        // When
        val result = service.isListingOwner(1L, "other@example.com")

        // Then
        assertFalse(result)
        verify(listingRepository).findById(1L)
    }

    @Test
    fun `test isListingOwner returns false when listing does not exist`() {
        // Given
        `when`(listingRepository.findById(1L)).thenReturn(Optional.empty())

        // When
        val result = service.isListingOwner(1L, "test@example.com")

        // Then
        assertFalse(result)
        verify(listingRepository).findById(1L)
    }

    @Test
    fun `test searchListings`() {
        // Given
        val searchRequest = ListingSearchRequestDto(
            q = "test",
            categoryId = 1L,
            condition = ListingCondition.NEW,
            minPrice = 50.0,
            maxPrice = 200.0
        )
        
        val pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"))
        val listings = listOf(testListing)
        val page = PageImpl(listings, pageable, listings.size.toLong())
        
        `when`(listingRepository.findAll(any<Specification<Listing>>(), any<PageRequest>())).thenReturn(page)

        // When
        val result = service.searchListings(searchRequest)

        // Then
        assertEquals(1, result.content.size)
        assertEquals("Test Listing", result.content[0].title)
        assertFalse(result.content[0].isBookmarked)
        verify(listingRepository).findAll(any<Specification<Listing>>(), any<PageRequest>())
    }
} 