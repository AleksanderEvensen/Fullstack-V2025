package edu.ntnu.fullstack.amazoom.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(name = "brief_description", nullable = false)
    var briefDescription: String,

    @Column(name = "full_description", nullable = false)
    var fullDescription: String,

    @Column(name = "category_id", nullable = false)
    var categoryId: Long,

    @Column(name = "seller_id", nullable = false)
    var sellerId: Long,

    @Column(nullable = false)
    var price: BigDecimal,

    var latitude: BigDecimal?,

    var longitude: BigDecimal?,

    @Column(nullable = false)
    var status: String = "ACTIVE",

    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)
