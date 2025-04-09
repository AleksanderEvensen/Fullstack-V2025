package edu.ntnu.fullstack.amazoom.common.entity

import jakarta.persistence.*

/**
 * Entity representing a security role in the system.
 * Used for role-based access control.
 */
@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    val name : RoleName
)

/**
 * Enumeration of available role names in the system.
 */
enum class RoleName {
    ROLE_USER,
    ROLE_ADMIN
}