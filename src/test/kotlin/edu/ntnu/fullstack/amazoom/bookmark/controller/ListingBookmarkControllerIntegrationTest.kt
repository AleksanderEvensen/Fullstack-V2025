package edu.ntnu.fullstack.amazoom.bookmark.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ntnu.fullstack.amazoom.auth.service.CustomUserDetails
import edu.ntnu.fullstack.amazoom.auth.service.JwtService
import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import edu.ntnu.fullstack.amazoom.bookmark.repository.ListingBookmarkRepository
import edu.ntnu.fullstack.amazoom.common.entity.Address
import edu.ntnu.fullstack.amazoom.common.entity.Role
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ListingBookmarkControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var listingRepository: ListingRepository

    @Autowired
    private lateinit var bookmarkRepository: ListingBookmarkRepository

    @Autowired
    private lateinit var jwtService: JwtService

    private lateinit var user: User
    private lateinit var sellerUser: User
    private lateinit var category: Category
    private lateinit var listing: Listing
    private lateinit var closeable: AutoCloseable
    private lateinit var jwtToken: String

    @BeforeEach
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)

        sellerUser = User(
            name = "Seller User",
            email = "seller@example.com",
            password = "password",
            phoneNumber = "1234567890",
            address = Address(
                streetName = "Seller Street",
                streetNumber = "456",
                postalCode = "54321",
                city = "Seller City",
                country = "Seller Country"
            ),
            roles = mutableSetOf(Role(id = 1, name = RoleName.ROLE_USER))
        )
        sellerUser = userRepository.save(sellerUser)

        // Create category
        category = Category(
            name = "Electronicss",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics-icon"
        )
        category = categoryRepository.save(category)

        // Create listing by seller
        listing = Listing(
            title = "Test Listing",
            category = category,
            condition = ListingCondition.NEW,
            seller = sellerUser,
            price = 100.0,
            description = "A test listing description",
            latitude = 20.0,
            longitude = 30.0,
        )
        listing = listingRepository.save(listing)

        // Create buyer user
        user = User(
            name = "Buyer User",
            email = "buyer@example.com",
            password = "password",
            phoneNumber = "0987654321",
            address = Address(
                streetName = "Buyer Street",
                streetNumber = "123",
                postalCode = "12345",
                city = "Buyer City",
                country = "Buyer Country"
            ),
            roles = mutableSetOf(Role(id = 1, name = RoleName.ROLE_USER))
        )
        user = userRepository.save(user)

        // Create JWT token for the buyer user
        val authorities = user.roles.map { SimpleGrantedAuthority(it.name.toString()) }
        val userDetails = CustomUserDetails(
            username = user.email,
            password = user.password,
            authorities = authorities
        )
        jwtToken = jwtService.generateToken(userDetails)

        // Set authentication for the buyer
        val auth: Authentication = TestingAuthenticationToken(user, null, "ROLE_USER")
        SecurityContextHolder.getContext().authentication = auth
    }

    @AfterEach
    fun cleanup() {
        bookmarkRepository.deleteAll()
        listingRepository.deleteAll()
        categoryRepository.deleteAll()
        userRepository.deleteAll()
        SecurityContextHolder.clearContext()
        closeable.close()
    }

    @Test
    fun testCreateBookmarkHappyPath() {
        // Create a bookmark with JWT Authentication
        mockMvc.perform(
            post("/api/bookmarks/${listing.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)

        // Verify bookmark exists in repository
        val bookmarks = bookmarkRepository.findAllForUser(user.id)
        assert(bookmarks.size == 1)
        assert(bookmarks[0].listing.id == listing.id)
        assert(bookmarks[0].user.id == user.id)
    }

    @Test
    fun testGetAllBookmarksForUser() {
        // Create a bookmark first
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )
        bookmarkRepository.save(bookmark)

        // Test getting all bookmarks with JWT Authentication
        mockMvc.perform(
            get("/api/bookmarks")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun testDeleteBookmark() {
        // Create a bookmark first
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )
        bookmarkRepository.save(bookmark)

        // Test deleting the bookmark with JWT Authentication
        mockMvc.perform(
            delete("/api/bookmarks/${listing.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)

        // Verify bookmark was deleted
        val bookmarks = bookmarkRepository.findAllForUser(user.id)
        assert(bookmarks.isEmpty())
    }

    @Test
    fun testCannotBookmarkOwnListing() {
        // Create a listing for the current user (trying to bookmark own listing)
        val ownListing = Listing(
            title = "My Own Listing",
            category = category,
            condition = ListingCondition.NEW,
            seller = user, // Same as authenticated user
            price = 200.0,
            description = "My own listing that I can't bookmark",
            latitude = 10.0,
            longitude = 20.0,
        )
        listingRepository.save(ownListing)

        // Try to create a bookmark for own listing with JWT Authentication, should fail
        mockMvc.perform(
            post("/api/bookmarks/${ownListing.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotBookmarkTwice() {
        // Create a bookmark first
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )
        bookmarkRepository.save(bookmark)

        // Try to create same bookmark again with JWT Authentication, should fail
        mockMvc.perform(
            post("/api/bookmarks/${listing.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isConflict)
    }
}