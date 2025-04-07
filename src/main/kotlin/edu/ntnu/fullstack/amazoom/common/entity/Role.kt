package edu.ntnu.fullstack.amazoom.common.entity

import jakarta.persistence.*

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

enum class RoleName {
    ROLE_USER,
    ROLE_ADMIN
}