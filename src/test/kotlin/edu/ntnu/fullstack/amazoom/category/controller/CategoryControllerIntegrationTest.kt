package edu.ntnu.fullstack.amazoom.category.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.ntnu.fullstack.amazoom.auth.service.CustomUserDetails
import edu.ntnu.fullstack.amazoom.auth.service.JwtService
import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequestDto
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import edu.ntnu.fullstack.amazoom.common.entity.Address
import edu.ntnu.fullstack.amazoom.common.entity.Role
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.repository.RoleRepository
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
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
import kotlin.jvm.optionals.getOrElse

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoryControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var roleRepository: RoleRepository

    private lateinit var adminUser: User
    private lateinit var category: Category
    private lateinit var closeable: AutoCloseable
    private lateinit var jwtToken: String

    @BeforeEach
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)

        val adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
            ?: roleRepository.save(Role(name = RoleName.ROLE_ADMIN))

        val existingUser = userRepository.findByEmail("admin@example.com")

        // Create admin user if it doesn't exist
        adminUser = existingUser.getOrElse {
            userRepository.save(User(
                name = "Admin User",
                email = "admin@example.com",
                password = "adminpassword",
                phoneNumber = "12345678",
                roles = mutableSetOf(adminRole),

            ))
        }

        // Create category (avoid duplication by using a unique name for each test run)
        val categoryName = "Electronics-${System.currentTimeMillis()}"
        category = Category(
            name = categoryName,
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics-icon"
        )
        category = categoryRepository.save(category)

        // Create JWT token for authentication
        val authorities = adminUser.roles.map { SimpleGrantedAuthority(it.name.toString()) }
        val userDetails = CustomUserDetails(
            username = adminUser.email,
            password = adminUser.password,
            authorities = authorities
        )
        jwtToken = jwtService.generateToken(userDetails)

        // Set authentication
        val auth: Authentication = TestingAuthenticationToken(adminUser, null, "ROLE_ADMIN")
        SecurityContextHolder.getContext().authentication = auth
    }

    @AfterEach
    fun cleanup() {
        try {
            // Clear test data - be thorough with cleanup
            categoryRepository.deleteAll()
            // Don't delete users and roles to prevent unique constraint issues
            // on subsequent test runs - instead we'll reuse existing ones

            SecurityContextHolder.clearContext()
            closeable.close()
        } catch (e: Exception) {
            // Log cleanup errors but don't fail the test
            println("Cleanup error: ${e.message}")
        }
    }

    @Test
    fun `test create category`() {
        val request = CreateOrUpdateCategoryRequestDto(
            name = "Books",
            description = "Books and literature",
            translationString = "books",
            icon = "books-icon"
        )

        mockMvc.perform(
            post("/api/categories")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value(request.name))
            .andExpect(jsonPath("$.description").value(request.description))
            .andExpect(jsonPath("$.translationString").value(request.translationString))
            .andExpect(jsonPath("$.icon").value(request.icon))
    }

    @Test
    fun `test get category`() {
        mockMvc.perform(
            get("/api/categories/${category.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(category.id))
            .andExpect(jsonPath("$.name").value(category.name))
            .andExpect(jsonPath("$.description").value(category.description))
            .andExpect(jsonPath("$.translationString").value(category.translationString))
            .andExpect(jsonPath("$.icon").value(category.icon))
    }

    @Test
    fun `test update category`() {
        val request = CreateOrUpdateCategoryRequestDto(
            name = "Updated Electronics",
            description = "Updated description",
            translationString = "updated_electronics",
            icon = "updated-icon"
        )

        mockMvc.perform(
            put("/api/categories/${category.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(category.id))
            .andExpect(jsonPath("$.name").value(request.name))
            .andExpect(jsonPath("$.description").value(request.description))
            .andExpect(jsonPath("$.translationString").value(request.translationString))
            .andExpect(jsonPath("$.icon").value(request.icon))
    }

    @Test
    fun `test delete category`() {
        mockMvc.perform(
            delete("/api/categories/${category.id}")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
    }

    @Test
    fun `test list all categories`() {
        mockMvc.perform(
            get("/api/categories")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(category.id))
            .andExpect(jsonPath("$[0].name").value(category.name))
            .andExpect(jsonPath("$[0].description").value(category.description))
            .andExpect(jsonPath("$[0].translationString").value(category.translationString))
            .andExpect(jsonPath("$[0].icon").value(category.icon))
    }

    @Test
    fun `test get category not found`() {
        mockMvc.perform(
            get("/api/categories/9999")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `test delete category not found`() {
        mockMvc.perform(
            delete("/api/categories/9999")
                .header("Authorization", "Bearer $jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }
}