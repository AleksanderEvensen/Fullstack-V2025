package edu.ntnu.fullstack.amazoom.common.entity

import edu.ntnu.fullstack.amazoom.common.dto.AddressDto
import edu.ntnu.fullstack.amazoom.common.dto.FullUserDto
import edu.ntnu.fullstack.amazoom.common.dto.UserDto
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

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

    @Version
    val version: Long = 0,
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

    fun toFullDto(): FullUserDto {
        return FullUserDto(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            phoneNumber = phoneNumber,
            profileImageUrl = profileImageUrl,
            address = address?.toDto()
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
) {
    fun toDto(): AddressDto {
        return AddressDto(
            streetName = streetName,
            streetNumber = streetNumber,
            postalCode = postalCode,
            city = city,
            country = country
        )
    }
}