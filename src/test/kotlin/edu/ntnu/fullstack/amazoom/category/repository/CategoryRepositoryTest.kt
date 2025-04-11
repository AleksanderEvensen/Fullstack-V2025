package edu.ntnu.fullstack.amazoom.category.repository

import edu.ntnu.fullstack.amazoom.category.entity.Category
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryRepositoryTest {
    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Test
    fun `test save and find category`() {
        val category = Category(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics_icon.png"
        )

        val savedCategory = categoryRepository.save(category)
        val foundCategory = categoryRepository.findById(savedCategory.id)

        assertTrue(foundCategory.isPresent)
        assertEquals(savedCategory.id, foundCategory.get().id)
        assertEquals(category.name, foundCategory.get().name)
        assertEquals(category.description, foundCategory.get().description)
        assertEquals(category.translationString, foundCategory.get().translationString)
        assertEquals(category.icon, foundCategory.get().icon)
    }

    @Test
    fun `test find all categories`() {
        val category1 = Category(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics_icon.png"
        )
        val category2 = Category(
            name = "Books",
            description = "Books and literature",
            translationString = "books",
            icon = "books_icon.png"
        )

        categoryRepository.save(category1)
        categoryRepository.save(category2)

        val categories = categoryRepository.findAll()

        assertEquals(2, categories.size)
        assertTrue(categories.any { it.name == category1.name })
        assertTrue(categories.any { it.name == category2.name })
    }

    @Test
    fun `test delete category`() {
        val category = Category(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics_icon.png"
        )

        val savedCategory = categoryRepository.save(category)
        categoryRepository.deleteById(savedCategory.id)

        assertFalse(categoryRepository.existsById(savedCategory.id))
    }

} 