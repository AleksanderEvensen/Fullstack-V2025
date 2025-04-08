package edu.ntnu.fullstack.amazoom.category.entity

import edu.ntnu.fullstack.amazoom.category.dto.CategoryDto
import jakarta.persistence.*

@Entity(name = "categories")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val translationString: String,

    @Column(nullable = false)
    val icon: String,
) {
    fun toDto(): CategoryDto {
        return CategoryDto(
            name = name,
            description = description,
            translationString = translationString,
            icon = icon
        )
    }
}