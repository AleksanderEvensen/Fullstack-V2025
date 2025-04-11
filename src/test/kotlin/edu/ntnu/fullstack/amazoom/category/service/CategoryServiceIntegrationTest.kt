package edu.ntnu.fullstack.amazoom.category.service

import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequestDto
import edu.ntnu.fullstack.amazoom.category.exception.CategoryNotFoundException
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryServiceIntegrationTest {
    @Autowired
    private lateinit var categoryService: CategoryService

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var adminUser: User

    @BeforeEach
    fun setup() {
        // Create admin user
        adminUser = userRepository.save(
            User(
                name = "Admin",
                email = "admin@example.com",
                password = "password123",
                phoneNumber = "12345678"
            )
        )

        // Set authentication for the buyer
        val auth: Authentication = TestingAuthenticationToken(adminUser, null, "ROLE_ADMIN")
        SecurityContextHolder.getContext().authentication = auth
    }

    @Test
    fun `test create category`() {
        val request = CreateOrUpdateCategoryRequestDto(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics_icon.png"
        )

        val response = categoryService.createCategory(request)

        assertNotNull(response.id)
        assertEquals(request.name, response.name)
        assertEquals(request.description, response.description)
        assertEquals(request.translationString, response.translationString)
        assertEquals(request.icon, response.icon)
    }

    @Test
    fun `test get category`() {
        // First create a category
        val request = CreateOrUpdateCategoryRequestDto(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics_icon.png"
        )
        val createdCategory = categoryService.createCategory(request)

        // Then retrieve it
        val retrievedCategory = categoryService.getCategory(createdCategory.id)

        assertNotNull(retrievedCategory)
        assertEquals(createdCategory.id, retrievedCategory.id)
        assertEquals(request.name, retrievedCategory.name)
        assertEquals(request.description, retrievedCategory.description)
        assertEquals(request.translationString, retrievedCategory.translationString)
        assertEquals(request.icon, retrievedCategory.icon)
    }

    @Test
    fun `test update category`() {
        // First create a category
        val createRequest = CreateOrUpdateCategoryRequestDto(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics_icon.png"
        )
        val createdCategory = categoryService.createCategory(createRequest)

        // Then update it
        val updateRequest = CreateOrUpdateCategoryRequestDto(
            name = "Updated Electronics",
            description = "Updated description",
            translationString = "updated_electronics",
            icon = "updated_icon.png"
        )
        val updatedCategory = categoryService.updateCategory(createdCategory.id, updateRequest)

        assertNotNull(updatedCategory)
        assertEquals(createdCategory.id, updatedCategory.id)
        assertEquals(updateRequest.name, updatedCategory.name)
        assertEquals(updateRequest.description, updatedCategory.description)
        assertEquals(updateRequest.translationString, updatedCategory.translationString)
        assertEquals(updateRequest.icon, updatedCategory.icon)
    }

    @Test
    fun `test delete category`() {
        // First create a category
        val request = CreateOrUpdateCategoryRequestDto(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics_icon.png"
        )
        val createdCategory = categoryService.createCategory(request)

        // Then delete it
        categoryService.deleteCategory(createdCategory.id)

        // Verify it's deleted
        assertThrows(CategoryNotFoundException::class.java) {
            categoryService.getCategory(createdCategory.id)
        }
    }

    @Test
    fun `test list all categories`() {
        // Create multiple categories
        val request1 = CreateOrUpdateCategoryRequestDto(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics_icon.png"
        )
        categoryService.createCategory(request1)

        val request2 = CreateOrUpdateCategoryRequestDto(
            name = "Books",
            description = "Books and literature",
            translationString = "books",
            icon = "books_icon.png"
        )
        categoryService.createCategory(request2)

        // List all categories
        val categories = categoryService.listAllCategories()

        assertEquals(2, categories.size)
        assertTrue(categories.any { it.name == request1.name })
        assertTrue(categories.any { it.name == request2.name })
    }
} 