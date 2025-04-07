package edu.ntnu.fullstack.amazoom.auth.entity

import edu.ntnu.fullstack.amazoom.common.entity.User
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val token: String,

    @Column(nullable = false)
    val expiresAt: Instant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)