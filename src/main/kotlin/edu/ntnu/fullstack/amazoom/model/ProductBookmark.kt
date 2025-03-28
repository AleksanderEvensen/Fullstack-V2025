package edu.ntnu.fullstack.amazoom.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "product_bookmark")
data class ProductBookmark(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "product_id", nullable = false)
    var productId: Long,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null
) 