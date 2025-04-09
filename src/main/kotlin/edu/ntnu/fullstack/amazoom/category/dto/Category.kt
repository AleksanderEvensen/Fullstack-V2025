package edu.ntnu.fullstack.amazoom.category.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data Transfer Object (DTO) for a category.
 * Contains the basic information about a product category.
 */
@Schema(description = "Product category information")
data class CategoryDto(
    @field:Schema(description = "Unique identifier of the category", example = "1")
    val id: Long,

    @field:Schema(description = "Name of the category", example = "Electronics")
    val name: String,

    @field:Schema(description = "Description of the category", example = "Electronic devices and equipment")
    val description: String,

    @field:Schema(description = "Translation key for i18n", example = "categories.electronics")
    val translationString: String,

    @field:Schema(description = "Icon identifier for the category", example = "Laptop")
    val icon: String
)