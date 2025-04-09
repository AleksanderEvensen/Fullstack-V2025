package edu.ntnu.fullstack.amazoom.listing.entity

import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.listing.dto.ListingDto
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * Entity representing a product listing in the system.
 * Contains details about a product being sold.
 */
@Entity
@Table(name = "listings")
data class Listing(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    // Basic Information
    @Column(nullable = false)
    val title: String,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "listing_condition")
    val condition: ListingCondition,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "seller_id", nullable = false)
    val seller: User,

    @Column(nullable = false)
    val price: Double,

    val originalPrice: Double? = null,

    @Column(columnDefinition = "TEXT", nullable = false)
    val description: String,

    // Product Details
    val modelYear: Int? = null,
    val manufacturer: String? = null,
    val model: String? = null,
    val serialNumber: String? = null,

    val purchaseDate: String? = null,
    val usageDuration: String? = null,

    @ElementCollection()
    @CollectionTable(name = "listing_defects", joinColumns = [JoinColumn(name = "listing_id")])
    @Column(name = "defect")
    val defects: List<String> = emptyList(),

    @ElementCollection
    @CollectionTable(name = "listing_modifications", joinColumns = [JoinColumn(name = "listing_id")])
    @Column(name = "modification")
    val modifications: List<String> = emptyList(),

    val reasonForSelling: String? = null,

    // Images
    @ElementCollection
    @CollectionTable(name = "listing_images", joinColumns = [JoinColumn(name = "listing_id")])
    @Column(name = "image_url")
    val images: List<String> = emptyList(),

    // creation and update timestamps
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    /**
     * Updates the updatedAt timestamp before persisting changes.
     */
    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}

/**
 * Enumeration of possible conditions for a listed item.
 */
@Schema(description = "Condition of a listed item")
enum class ListingCondition {
    @Schema(description = "Brand new, unused item")
    NEW,

    @Schema(description = "Almost new with no visible wear")
    LIKE_NEW,

    @Schema(description = "Used but in excellent condition with minimal wear")
    VERY_GOOD,

    @Schema(description = "Used with some noticeable wear but fully functional")
    GOOD,

    @Schema(description = "Used with significant wear but still functional")
    ACCEPTABLE
}