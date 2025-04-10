package edu.ntnu.fullstack.amazoom.listing.repository

import edu.ntnu.fullstack.amazoom.common.entity.Address
import edu.ntnu.fullstack.amazoom.common.entity.Role
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.entity.ListingStatus
import edu.ntnu.fullstack.amazoom.category.entity.Category
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ListingRepositoryTest {

    @Autowired
    private lateinit var repository: ListingRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    private lateinit var user: User
    private lateinit var category: Category
    private lateinit var listing: Listing

    @BeforeEach
    fun setup() {
        val role = Role(
            name = RoleName.ROLE_USER
        )
        entityManager.persist(role)

        // Create and persist a user
        user = User(
            name = "Test User",
            email = "test.user@example.com",
            password = "password",
            phoneNumber = "1234567890",
            address = Address(
                streetName = "Test Street",
                streetNumber = "123",
                postalCode = "12345",
                city = "Test City",
                country = "Test Country"
            ),
            roles = mutableSetOf(role) // Associate the persisted role
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
            description = "A test listing",
            latitude = 20.0,
            longitude = 30.0,
        )
        entityManager.persist(listing)
        entityManager.flush()
    }

    @Test
    fun `test save and find listing`() {
        // Create a new listing
        val newListing = Listing(
            title = "Another Test Listing",
            category = category,
            condition = ListingCondition.GOOD,
            seller = user,
            price = 200.0,
            description = "Another test listing description",
            latitude = 25.0,
            longitude = 35.0,
        )

        val savedListing = repository.save(newListing)
        val foundListing = repository.findById(savedListing.id)

        assertTrue(foundListing.isPresent)
        assertEquals(newListing.title, foundListing.get().title)
        assertEquals(newListing.price, foundListing.get().price)
        assertEquals(newListing.seller.id, foundListing.get().seller.id)
    }

    @Test
    fun `test delete listing`() {
        // Create a new listing
        val newListing = Listing(
            title = "Listing to Delete",
            category = category,
            condition = ListingCondition.GOOD,
            seller = user,
            price = 300.0,
            description = "A listing to delete",
            latitude = 40.0,
            longitude = 50.0,
        )
        val savedListing = repository.save(newListing)

        // Delete the listing
        repository.delete(savedListing)

        // Verify the listing was deleted
        val deletedListing = repository.findById(savedListing.id)
        assertFalse(deletedListing.isPresent)
    }

    @Test
    fun `test update listing`() {
        // Create a new listing
        val originalListing = Listing(
            title = "Original Title",
            category = category,
            condition = ListingCondition.NEW,
            seller = user,
            price = 100.0,
            description = "Original description",
            latitude = 10.0,
            longitude = 20.0,
        )
        val savedListing = repository.save(originalListing)

        // Update the listing
        val updatedListing = savedListing.copy(
            title = "Updated Title",
            price = 150.0,
            description = "Updated description"
        )
        repository.save(updatedListing)

        // Verify the listing was updated
        val foundListing = repository.findById(savedListing.id)
        assertTrue(foundListing.isPresent)
        assertEquals("Updated Title", foundListing.get().title)
        assertEquals(150.0, foundListing.get().price)
        assertEquals("Updated description", foundListing.get().description)
    }

    @Test
    fun `test find all listings with pagination and sorting`() {
        // Create additional listings
        val listing2 = Listing(
            title = "Cheap Listing",
            category = category,
            condition = ListingCondition.GOOD,
            seller = user,
            price = 50.0,
            description = "A cheap listing",
            latitude = 15.0,
            longitude = 25.0,
        )
        entityManager.persist(listing2)

        val listing3 = Listing(
            title = "Expensive Listing",
            category = category,
            condition = ListingCondition.LIKE_NEW,
            seller = user,
            price = 500.0,
            description = "An expensive listing",
            latitude = 30.0,
            longitude = 40.0,
        )
        entityManager.persist(listing3)
        entityManager.flush()

        // Test pagination and sorting by price ascending
        val pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "price"))
        val result = repository.findAll(pageable)

        assertEquals(2, result.content.size)
        assertEquals("Cheap Listing", result.content[0].title)
        assertEquals("Test Listing", result.content[1].title)
        assertEquals(3, result.totalElements)

        // Test pagination and sorting by price descending
        val pageableDesc = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "price"))
        val resultDesc = repository.findAll(pageableDesc)

        assertEquals(2, resultDesc.content.size)
        assertEquals("Expensive Listing", resultDesc.content[0].title)
        assertEquals("Test Listing", resultDesc.content[1].title)
    }

    @Test
    fun `test find listings by seller`() {
        // Create additional user and listing
        val otherUser = User(
            name = "Other User",
            email = "other.user@example.com",
            password = "password",
            phoneNumber = "9876543210",
            address = Address(
                streetName = "Other Street",
                streetNumber = "456",
                postalCode = "54321",
                city = "Other City",
                country = "Other Country"
            ),
            roles = mutableSetOf(Role(id = 1, name = RoleName.ROLE_USER))
        )
        entityManager.persist(otherUser)

        val otherListing = Listing(
            title = "Other User Listing",
            category = category,
            condition = ListingCondition.GOOD,
            seller = otherUser,
            price = 150.0,
            description = "A listing from other user",
            latitude = 35.0,
            longitude = 45.0,
        )
        entityManager.persist(otherListing)

        // Test finding all listings
        val allListings = repository.findAll()
        assertTrue(allListings.size >= 2)
        
        // Verify both users' listings are in the result
        assertTrue(allListings.any { it.seller.id == user.id })
        assertTrue(allListings.any { it.seller.id == otherUser.id })

    }
} 