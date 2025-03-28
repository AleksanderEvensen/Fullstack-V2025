package edu.ntnu.fullstack.amazoom.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity(name = "category")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val description: String?,
    val parentId: Long?,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) 