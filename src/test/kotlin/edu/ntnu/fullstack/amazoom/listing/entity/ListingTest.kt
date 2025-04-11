package edu.ntnu.fullstack.amazoom.listing.entity

import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.common.entity.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

class ListingTest {
    @Test
    fun `test listing creation with all fields`() {
        // Create test user
        val user = User(
            name = "Test User",
            email = "test.user@example.com",
            password = "password123",
            phoneNumber = "12345678"
        )

        // Create test category
        val category = Category(
            name = "Test Category",
            description = "Test category description",
            translationString = "test_category",
            icon = "test_icon.png"
        )

        // Create listing with all fields
        val listing = Listing(
            title = "Test Listing",
            category = category,
            condition = ListingCondition.NEW,
            seller = user,
            price = 100.0,
            originalPrice = 150.0,
            description = "Test description",
            latitude = 63.446827,
            longitude = 10.421906,
            modelYear = 2023,
            manufacturer = "Test Manufacturer",
            model = "Test Model",
            serialNumber = "123456789",
            purchaseDate = "2023-01-01",
            usageDuration = "6 months",
            defects = listOf("Minor scratch"),
            modifications = listOf("Upgraded RAM"),
            reasonForSelling = "Upgrading to new model",
            status = ListingStatus.ACTIVE,
            images = listOf("image1.jpg", "image2.jpg")
        )

        // Verify basic properties
        assertEquals(0L, listing.id) // Default ID
        assertEquals("Test Listing", listing.title)
        assertEquals(category, listing.category)
        assertEquals(ListingCondition.NEW, listing.condition)
        assertEquals(user, listing.seller)
        assertEquals(100.0, listing.price)
        assertEquals(150.0, listing.originalPrice)
        assertEquals("Test description", listing.description)
        assertEquals(63.446827, listing.latitude)
        assertEquals(10.421906, listing.longitude)

        // Verify product details
        assertEquals(2023, listing.modelYear)
        assertEquals("Test Manufacturer", listing.manufacturer)
        assertEquals("Test Model", listing.model)
        assertEquals("123456789", listing.serialNumber)
        assertEquals("2023-01-01", listing.purchaseDate)
        assertEquals("6 months", listing.usageDuration)
        assertEquals(listOf("Minor scratch"), listing.defects)
        assertEquals(listOf("Upgraded RAM"), listing.modifications)
        assertEquals("Upgrading to new model", listing.reasonForSelling)

        // Verify status and images
        assertEquals(ListingStatus.ACTIVE, listing.status)
        assertEquals(listOf("image1.jpg", "image2.jpg"), listing.images)

        // Verify timestamps
        assertNotNull(listing.createdAt)
        assertNotNull(listing.updatedAt)
    }

    @Test
    fun `test listing creation with minimal fields`() {
        // Create test user
        val user = User(
            name = "Test User",
            email = "test.user@example.com",
            password = "password123",
            phoneNumber = "12345678"
        )

        // Create test category
        val category = Category(
            name = "Test Category",
            description = "Test category description",
            translationString = "test_category",
            icon = "test_icon.png"
        )

        // Create listing with minimal required fields
        val listing = Listing(
            title = "Test Listing",
            category = category,
            condition = ListingCondition.NEW,
            seller = user,
            price = 100.0,
            description = "Test description",
            latitude = 63.446827,
            longitude = 10.421906
        )

        // Verify required properties
        assertEquals(0L, listing.id)
        assertEquals("Test Listing", listing.title)
        assertEquals(category, listing.category)
        assertEquals(ListingCondition.NEW, listing.condition)
        assertEquals(user, listing.seller)
        assertEquals(100.0, listing.price)
        assertEquals("Test description", listing.description)
        assertEquals(63.446827, listing.latitude)
        assertEquals(10.421906, listing.longitude)

        // Verify optional properties are null or empty
        assertNull(listing.originalPrice)
        assertNull(listing.modelYear)
        assertNull(listing.manufacturer)
        assertNull(listing.model)
        assertNull(listing.serialNumber)
        assertNull(listing.purchaseDate)
        assertNull(listing.usageDuration)
        assertTrue(listing.defects.isEmpty())
        assertTrue(listing.modifications.isEmpty())
        assertNull(listing.reasonForSelling)
        assertEquals(ListingStatus.ACTIVE, listing.status)
        assertTrue(listing.images.isEmpty())
    }

    @Test
    fun `test listing creation with id`() {
        // Create test user
        val user = User(
            name = "Test User",
            email = "test.user@example.com",
            password = "password123",
            phoneNumber = "12345678"
        )

        // Create test category
        val category = Category(
            name = "Test Category",
            description = "Test category description",
            translationString = "test_category",
            icon = "test_icon.png"
        )

        // Create listing with specific ID
        val listing = Listing(
            id = 1L,
            title = "Test Listing",
            category = category,
            condition = ListingCondition.NEW,
            seller = user,
            price = 100.0,
            description = "Test description",
            latitude = 63.446827,
            longitude = 10.421906
        )

        // Verify ID and other properties
        assertEquals(1L, listing.id)
        assertEquals("Test Listing", listing.title)
        assertEquals(category, listing.category)
        assertEquals(ListingCondition.NEW, listing.condition)
        assertEquals(user, listing.seller)
        assertEquals(100.0, listing.price)
        assertEquals("Test description", listing.description)
        assertEquals(63.446827, listing.latitude)
        assertEquals(10.421906, listing.longitude)
    }
} 