package edu.ntnu.fullstack.amazoom.common.entity

import edu.ntnu.fullstack.amazoom.auth.entity.RefreshToken
import edu.ntnu.fullstack.amazoom.common.dto.UserDto
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val firstName: String,

    @Column(nullable = false)
    val lastName: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, unique = true)
    val phoneNumber: String,

    @Embedded
    val address: Address? = null,

    val profileImageUrl: String? = null,

    val createdAt: Instant = Instant.now(),

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: MutableSet<Role> = mutableSetOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val refreshTokens: MutableList<RefreshToken> = mutableListOf()
) {
    fun getFullName() = "$firstName $lastName"

    fun toDto(): UserDto {
        return UserDto(
            id,
            firstName,
            lastName,
            profileImageUrl
        )
    }
}

@Embeddable
data class Address(
    val streetName: String,

    val streetNumber: String,

    val postalCode: String,

    val city: String,

    val country: String
)