package edu.ntnu.fullstack.amazoom.bookmark.service

import edu.ntnu.fullstack.amazoom.bookmark.dto.CreateListingBookmarkRequestDto
import edu.ntnu.fullstack.amazoom.bookmark.dto.ListingBookmarkResponseDto
import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkAlreadyExistsException
import edu.ntnu.fullstack.amazoom.bookmark.exception.BookmarkOwnListingException
import edu.ntnu.fullstack.amazoom.bookmark.mapper.ListingBookmarkMapper
import edu.ntnu.fullstack.amazoom.bookmark.repository.ListingBookmarkRepository
import edu.ntnu.fullstack.amazoom.common.entity.Address
import edu.ntnu.fullstack.amazoom.common.entity.Role
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.exception.UnauthorizedException
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import edu.ntnu.fullstack.amazoom.common.service.UserService
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.exception.ListingNotFoundException
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ListingBookmarkServiceTest {
    @Mock
    private lateinit var listingBookmarkRepository: ListingBookmarkRepository
    
    @Mock
    private lateinit var listingRepository: ListingRepository
    
    @Mock
    private lateinit var userRepository: UserRepository
    
    @Mock
    private lateinit var userService: UserService
    
    @InjectMocks
    private lateinit var service: ListingBookmarkService

    private val testUser = User(
        id = 1L,
        firstName = "Test",
        lastName = "User",
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

    private val otherUser = User(
        id = 2L,
        firstName = "Other",
        lastName = "User",
        email = "other@example.com",
        phoneNumber = "0987654321",
        password = "password",
        address = Address(
            streetName = "Other Street",
            streetNumber = "321",
            postalCode = "54321",
            city = "Other City",
            country = "Other Country"
        ),
        roles = mutableSetOf(Role(id = 2, name = RoleName.ROLE_USER))
    )

    private val testListing = Listing(
        id = 1L,
        title = "Test Listing",
        description = "Test Description",
        price = 100.0,
        seller = otherUser,
        category = testCategory,
        condition = ListingCondition.NEW
    )

    private val testBookmark = ListingBookmark(
        id = 1L,
        listing = testListing,
        user = testUser
    )

    @BeforeEach
    fun setup() {
        `when`(userService.getCurrentUser()).thenReturn(testUser)
    }

    @Test
    fun `test create bookmark successfully`() {
        // Given
        val request = CreateListingBookmarkRequestDto(listingId = 1L)
        `when`(listingRepository.findById(1L)).thenReturn(Optional.of(testListing))
        `when`(listingBookmarkRepository.existsByUserIdAndListingId(1L, 1L)).thenReturn(false)
        `when`(listingBookmarkRepository.save(any())).thenReturn(testBookmark)

        // When
        val result = service.createBookmark(request)

        // Then
        assert(result is ListingBookmarkResponseDto)
        verify(listingRepository).findById(1L)
        verify(listingBookmarkRepository).existsByUserIdAndListingId(1L, 1L)
        verify(listingBookmarkRepository).save(any())
    }

    @Test
    fun `test createBookmarkByListingId successfully`() {
        // Given
        `when`(listingRepository.findById(1L)).thenReturn(Optional.of(testListing))
        `when`(listingBookmarkRepository.existsByUserIdAndListingId(1L, 1L)).thenReturn(false)
        `when`(listingBookmarkRepository.save(any())).thenReturn(testBookmark)

        // When
        val result = service.createBookmarkByListingId(1L)

        // Then
        assert(result is ListingBookmarkResponseDto)
        verify(listingRepository).findById(1L)
        verify(listingBookmarkRepository).existsByUserIdAndListingId(1L, 1L)
        verify(listingBookmarkRepository).save(any())
    }



    @Test
    fun `test create bookmark throws BookmarkAlreadyExistsException`() {
        // Given
        val request = CreateListingBookmarkRequestDto(listingId = 1L)
        `when`(listingRepository.findById(1L)).thenReturn(Optional.of(testListing))
        `when`(listingBookmarkRepository.existsByUserIdAndListingId(1L, 1L)).thenReturn(true)

        // When/Then
        assertThrows<BookmarkAlreadyExistsException> {
            service.createBookmark(request)
        }
    }

    @Test
    fun `test create bookmark throws BookmarkOwnListingException`() {
        // Given
        val ownListing = testListing.copy(seller = testUser)
        val request = CreateListingBookmarkRequestDto(listingId = 1L)
        
        `when`(listingRepository.findById(1L)).thenReturn(Optional.of(ownListing))
        `when`(listingBookmarkRepository.existsByUserIdAndListingId(1L, 1L)).thenReturn(false)

        // When/Then
        assertThrows<BookmarkOwnListingException> {
            service.createBookmark(request)
        }
    }

    @Test
    fun `test delete bookmark successfully`() {
        // Given
        `when`(listingBookmarkRepository.findById(1L)).thenReturn(Optional.of(testBookmark))
        doNothing().`when`(listingBookmarkRepository).deleteById(1L)

        // When
        service.deleteBookmark(1L)

        // Then
        verify(listingBookmarkRepository).findById(1L)
        verify(listingBookmarkRepository).deleteById(1L)
    }

    @Test
    fun `test delete bookmark throws UnauthorizedException`() {
        // Given
        val otherUserBookmark = testBookmark.copy(user = otherUser)
        `when`(listingBookmarkRepository.findById(1L)).thenReturn(Optional.of(otherUserBookmark))

        // When/Then
        assertThrows<UnauthorizedException> {
            service.deleteBookmark(1L)
        }
    }

    @Test
    fun `test delete bookmark throws ListingNotFoundException`() {
        // Given
        `when`(listingBookmarkRepository.findById(1L)).thenReturn(Optional.empty())

        // When/Then
        assertThrows<ListingNotFoundException> {
            service.deleteBookmark(1L)
        }
    }

    @Test
    fun `test delete bookmark by listing id successfully`() {
        // Given
        `when`(listingBookmarkRepository.findByUserIdAndListingId(1L, 1L)).thenReturn(testBookmark)
        doNothing().`when`(listingBookmarkRepository).deleteById(1L)

        // When
        service.deleteBookmarkByListingId(1L)

        // Then
        verify(listingBookmarkRepository).findByUserIdAndListingId(1L, 1L)
        verify(listingBookmarkRepository).deleteById(1L)
    }

    @Test
    fun `test delete bookmark by listing id throws ListingNotFoundException`() {
        // Given
        `when`(listingBookmarkRepository.findByUserIdAndListingId(1L, 1L)).thenReturn(null)

        // When/Then
        assertThrows<ListingNotFoundException> {
            service.deleteBookmarkByListingId(1L)
        }
    }

    @Test
    fun `test list all bookmarks for user`() {
        // Given
        val bookmarks = listOf(testBookmark)
        `when`(listingBookmarkRepository.findAllForUser(1L)).thenReturn(bookmarks)

        // When
        val result = service.listAllBookmarksForUser()

        // Then
        assert(result.isNotEmpty())
        verify(userService).getCurrentUser()
        verify(listingBookmarkRepository).findAllForUser(1L)
    }

    @Test
    fun `test check if listing is bookmarked by user returns true`() {
        // Given
        `when`(listingBookmarkRepository.existsByUserIdAndListingId(1L, 1L)).thenReturn(true)

        // When
        val result = service.isListingBookmarkedByUser(1L)

        // Then
        assert(result)
        verify(userService).getCurrentUser()
        verify(listingBookmarkRepository).existsByUserIdAndListingId(1L, 1L)
    }

    @Test
    fun `test check if listing is bookmarked by user returns false`() {
        // Given
        `when`(listingBookmarkRepository.existsByUserIdAndListingId(1L, 1L)).thenReturn(false)

        // When
        val result = service.isListingBookmarkedByUser(1L)

        // Then
        assert(!result)
        verify(userService).getCurrentUser()
        verify(listingBookmarkRepository).existsByUserIdAndListingId(1L, 1L)
    }
} 