package edu.ntnu.fullstack.amazoom.category.dto

/**
 * Data Transfer Object (DTO) for a category.
 * Contains the basic information about a product category.
 */
data class CategoryDto(
    val id: Long,
    val name: String,
    val description: String,
    val translationString: String,
    val icon: String
)