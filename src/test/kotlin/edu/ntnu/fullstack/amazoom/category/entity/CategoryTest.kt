package edu.ntnu.fullstack.amazoom.category.entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CategoryTest {
    @Test
    fun `test category creation with all fields`() {
        val category = Category(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics_icon.png"
        )

        assertEquals("Electronics", category.name)
        assertEquals("Electronic devices and gadgets", category.description)
        assertEquals("electronics", category.translationString)
        assertEquals("electronics_icon.png", category.icon)
        assertNull(category.version)
    }

    @Test
    fun `test category creation with minimal fields`() {
        val category = Category(
            name = "Books",
            description = "Books and literature",
            translationString = "books",
            icon = "books_icon.png"
        )

        assertEquals("Books", category.name)
        assertEquals("Books and literature", category.description)
        assertEquals("books", category.translationString)
        assertEquals("books_icon.png", category.icon)
        assertNull(category.version)
    }

    @Test
    fun `test category creation with id`() {
        val category = Category(
            id = 1L,
            name = "Clothing",
            description = "Clothing and accessories",
            translationString = "clothing",
            icon = "clothing_icon.png"
        )

        assertEquals(1L, category.id)
        assertEquals("Clothing", category.name)
        assertEquals("Clothing and accessories", category.description)
        assertEquals("clothing", category.translationString)
        assertEquals("clothing_icon.png", category.icon)
        assertNull(category.version)
    }
} 