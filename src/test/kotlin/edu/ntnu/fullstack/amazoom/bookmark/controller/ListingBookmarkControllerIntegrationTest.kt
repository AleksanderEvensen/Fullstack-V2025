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
import edu.ntnu.fullstack.amazoom.common.repository.RoleRepository
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.repository.ListingRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mapping.AccessOptions
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
    private lateinit var roleRepository: RoleRepository

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

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)
        val roleUser = roleRepository.findByName(RoleName.ROLE_USER) ?:
        roleRepository.save(Role(name = RoleName.ROLE_USER))

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
            roles = mutableSetOf(roleUser)
        )
        sellerUser = userRepository.save(sellerUser)

        category = Category(
            name = "Electronics${System.currentTimeMillis()}",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics-icon"
        )
        category = categoryRepository.save(category)

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
            roles = mutableSetOf(roleUser)
        )
        user = userRepository.save(user)

        val authorities = user.roles.map { SimpleGrantedAuthority(it.name.toString()) }
        val userDetails = CustomUserDetails(
            username = user.email,
            password = user.password,
            authorities = authorities
        )
        jwtToken = jwtService.generateToken(userDetails)
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
        val result = mockMvc.perform(
            post("/api/bookmarks/${listing.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andReturn()
        println("Response: " + result.response.contentAsString)
        entityManager.flush()

        val bookmarks = bookmarkRepository.findAllForUser(user.id)
        assertFalse(bookmarks.isEmpty(), "Expected at least one bookmark")
        assertTrue(bookmarks.any { it.listing.id == listing.id && it.user.id == user.id },
            "Expected to find the created bookmark")
    }

    @Test
    fun testGetAllBookmarksForUser() {
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )
        bookmarkRepository.save(bookmark)
        entityManager.flush()

        mockMvc.perform(
            get("/api/bookmarks")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].listing.id").value(listing.id))
    }

    @Test
    @Transactional()
    fun testDeleteBookmark() {
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )
        val savedBookmark = bookmarkRepository.save(bookmark)
        entityManager.flush()
        val bookmarkBeforeDelete = bookmarkRepository.findByUserIdAndListingId(user.id, listing.id)
        assertNotNull(bookmarkBeforeDelete, "Bookmark should exist before deletion")
        mockMvc.perform(
            delete("/api/bookmarks/${listing.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)

        entityManager.flush()
        entityManager.clear()


        val bookmarkAfterDelete = bookmarkRepository.findByUserIdAndListingId(user.id, listing.id)
        assertNull(bookmarkAfterDelete, "Expected bookmark to be deleted")
    }

    @Test
    fun testCannotBookmarkOwnListing() {
        val ownListing = Listing(
            title = "My Own Listing",
            category = category,
            condition = ListingCondition.NEW,
            seller = user,
            price = 200.0,
            description = "My own listing that I can't bookmark",
            latitude = 10.0,
            longitude = 20.0,
        )
        listingRepository.save(ownListing)
        entityManager.flush()

        mockMvc.perform(
            post("/api/bookmarks/${ownListing.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun testCannotBookmarkTwice() {
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )
        bookmarkRepository.save(bookmark)
        entityManager.flush()

        mockMvc.perform(
            post("/api/bookmarks/${listing.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isConflict)
    }
}