package edu.ntnu.fullstack.amazoom.bookmark.repository

import edu.ntnu.fullstack.amazoom.bookmark.entity.ListingBookmark
import edu.ntnu.fullstack.amazoom.common.entity.Address
import edu.ntnu.fullstack.amazoom.common.entity.Role
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.category.entity.Category
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ListingBookmarkRepositoryTest {

    @Autowired
    private lateinit var repository: ListingBookmarkRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    private lateinit var user: User
    private lateinit var category: Category
    private lateinit var listing: Listing

    @BeforeEach
    fun setup() {
        // Create and persist a user
        user = User(
            firstName = "Test",
            lastName = "User",
            email = "test.user@example.com",
            phoneNumber = "1234567890",
            address = Address(
                streetName = "Test Street",
                streetNumber = "123",
                postalCode = "12345",
                city = "Test City",
                country = "Test Country"
            )
        )
        entityManager.persist(user)
        
        // Create and persist a category
        category = Category(
            name = "Electronics",
            description = "Electronic devices and gadgets",
            translationString = "electronics",
            icon = "electronics-icon"
        )
        entityManager.persist(category)
        
        // Create and persist a listing
        listing = Listing(
            title = "Test Listing",
            category = category,
            condition = ListingCondition.NEW,
            seller = user,
            price = 100.0,
            description = "A test listing"
        )
        entityManager.persist(listing)
        entityManager.flush()
    }

    @Test
    fun `test save and find bookmark`() {
        // Create bookmark using persisted entities
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )

        val savedBookmark = repository.save(bookmark)
        val foundBookmark = repository.findById(savedBookmark.id)

        assertTrue(foundBookmark.isPresent)
        assertEquals(bookmark.user.id, foundBookmark.get().user.id)
        assertEquals(bookmark.listing.id, foundBookmark.get().listing.id)
    }

    @Test
    fun `test existsByUserIdAndListingId`() {
        // Create bookmark using persisted entities
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )
        repository.save(bookmark)

        assertTrue(repository.existsByUserIdAndListingId(user.id, listing.id))
        assertFalse(repository.existsByUserIdAndListingId(user.id, 9999L))
    }

    @Test
    fun `test findByUserIdAndListingId`() {
        // Create bookmark using persisted entities
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )
        repository.save(bookmark)

        val foundBookmark = repository.findByUserIdAndListingId(user.id, listing.id)
        assertNotNull(foundBookmark)
        assertEquals(user.id, foundBookmark?.user?.id)
        assertEquals(listing.id, foundBookmark?.listing?.id)
    }

    @Test
    fun `test delete bookmark`() {
        // Create bookmark using persisted entities
        val bookmark = ListingBookmark(
            user = user,
            listing = listing
        )
        val savedBookmark = repository.save(bookmark)

        repository.delete(savedBookmark)

        assertFalse(repository.existsById(savedBookmark.id))
    }
    
    @Test
    fun `test findAllForUser`() {
        // Create bookmarks using persisted entities
        val bookmark1 = ListingBookmark(
            user = user,
            listing = listing
        )
        repository.save(bookmark1)
        
        // Create second listing
        val listing2 = Listing(
            title = "Second Test Listing",
            category = category,
            condition = ListingCondition.GOOD,
            seller = user,
            price = 200.0,
            description = "Another test listing"
        )
        entityManager.persist(listing2)
        
        val bookmark2 = ListingBookmark(
            user = user,
            listing = listing2
        )
        repository.save(bookmark2)
        
        val bookmarks = repository.findAllForUser(user.id)
        assertEquals(2, bookmarks.size)
    }
}