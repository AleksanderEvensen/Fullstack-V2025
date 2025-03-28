package edu.ntnu.fullstack.amazoom.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "product_image")
data class ProductImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "product_id", nullable = false)
    var productId: Long,

    @Column(name = "image_url", nullable = false)
    var imageUrl: String,

    @Column(name = "is_primary", nullable = false)
    var isPrimary: Boolean = false,

    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null
) 