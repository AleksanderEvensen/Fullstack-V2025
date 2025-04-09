package edu.ntnu.fullstack.amazoom.category.dto

import jakarta.validation.constraints.NotBlank

/**
 * Data Transfer Object (DTO) for creating or updating a category.
 * Contains all fields that can be set when creating or updating a category.
 */
data class CreateOrUpdateCategoryRequestDto(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Description is required")
    val description: String,

    @field:NotBlank(message = "Translation string is required")
    val translationString: String,

    @field:NotBlank(message = "Icon is required")
    val icon: String
)