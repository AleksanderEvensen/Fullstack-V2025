package edu.ntnu.fullstack.amazoom.listing.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ntnu.fullstack.amazoom.auth.service.CustomUserDetails
import edu.ntnu.fullstack.amazoom.auth.service.JwtService
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import edu.ntnu.fullstack.amazoom.common.entity.Address
import edu.ntnu.fullstack.amazoom.common.entity.Role
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.repository.RoleRepository
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequestDto
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
class ListingControllerIntegrationTest {

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
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var roleRepository: RoleRepository

    private lateinit var user: User
    private lateinit var category: Category
    private lateinit var listing: Listing
    private lateinit var closeable: AutoCloseable
    private lateinit var jwtToken: String

    @BeforeEach
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)

        val userRole = roleRepository.findByName(RoleName.ROLE_USER)
            ?: roleRepository.save(Role(name = RoleName.ROLE_USER))


        // Create user
        user = User(
            name = "Test User",
            email = "test@example.com",
            password = "password",
            phoneNumber = "1234567890",
            address = Address(
                streetName = "Test Street",
                streetNumber = "123",
                postalCode = "12345",
                city = "Test City",
                country = "Test Country"
            ),
            roles = mutableSetOf(userRole)
        )
        user = userRepository.save(user)

        // Create category
        category = Category(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics-icon"
        )
        category = categoryRepository.save(category)

        // Create listing
        listing = Listing(
            title = "Test Listing",
            category = category,
            condition = ListingCondition.NEW,
            seller = user,
            price = 100.0,
            description = "A test listing description",
            latitude = 20.0,
            longitude = 30.0
        )
        listing = listingRepository.save(listing)

        // Create JWT token for authentication
        val authorities = user.roles.map { SimpleGrantedAuthority(it.name.toString()) }
        val userDetails = CustomUserDetails(
            username = user.email,
            password = user.password,
            authorities = authorities
        )
        jwtToken = jwtService.generateToken(userDetails)

        // Set authentication
        val auth: Authentication = TestingAuthenticationToken(user, null, "ROLE_USER")
        SecurityContextHolder.getContext().authentication = auth
    }

    @AfterEach
    fun cleanup() {
        listingRepository.deleteAll()
        categoryRepository.deleteAll()
        userRepository.deleteAll()
        SecurityContextHolder.clearContext()
        closeable.close()
    }




    @Test
    fun testDeleteListingNotFound() {
        mockMvc.perform(
            delete("/api/listings/9999")  // Non-existent ID
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isForbidden)
    }
} 