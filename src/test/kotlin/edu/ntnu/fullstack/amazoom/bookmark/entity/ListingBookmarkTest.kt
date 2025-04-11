package edu.ntnu.fullstack.amazoom.bookmark.entity

import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ListingBookmarkTest {
    @Test
    fun `test bookmark creation`() {
        // Create test user
        val user = User(
            name = "Test User",
            email = "test.user@example.com",
            password = "password123",
            phoneNumber = "12345678"
        )

        // Create test listing
        val listing = Listing(
            title = "Test Listing",
            description = "Test description",
            price = 100.0,
            condition = ListingCondition.NEW,
            seller = user,
            latitude = 0.0,
            longitude = 0.0,
            category = Category(
                name = "Test Category",
                description = "Test category description",
                translationString = "test_category",
                icon = "test_icon.png"
            )
        )

        // Create bookmark
        val bookmark = ListingBookmark(
            listing = listing,
            user = user
        )

        // Verify properties
        assertEquals(0L, bookmark.id) // Default ID
        assertEquals(listing, bookmark.listing)
        assertEquals(user, bookmark.user)
    }

    @Test
    fun `test bookmark creation with id`() {
        // Create test user
        val user = User(
            name = "Test User",
            email = "test.user@example.com",
            password = "password123",
            phoneNumber = "12345678",

        )

        // Create test listing
        val listing = Listing(
            title = "Test Listing",
            description = "Test description",
            price = 100.0,
            condition = ListingCondition.NEW,
            seller = user,
            latitude = 0.0,
            longitude = 0.0,
            category = Category(
                name = "Test Category",
                description = "Test category description",
                translationString = "test_category",
                icon = "test_icon.png"
            )
        )

        // Create bookmark with specific ID
        val bookmark = ListingBookmark(
            id = 1L,
            listing = listing,
            user = user
        )

        // Verify properties
        assertEquals(1L, bookmark.id)
        assertEquals(listing, bookmark.listing)
        assertEquals(user, bookmark.user)
    }
} 