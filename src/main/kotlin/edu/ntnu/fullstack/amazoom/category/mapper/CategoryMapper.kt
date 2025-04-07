package edu.ntnu.fullstack.amazoom.category.mapper

import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequest
import edu.ntnu.fullstack.amazoom.category.dto.CategoryDto
import edu.ntnu.fullstack.amazoom.category.entity.Category

object CategoryMapper {

    /**
     * Converts a CreateOrUpdateCategoryRequest to a Category entity.
     */
    fun toEntity(request: CreateOrUpdateCategoryRequest): Category {
        return Category(
            name = request.name,
            description = request.description,
            translationString = request.translationString,
            icon = request.icon
        )
    }

    /**
     * Copies updated fields onto an existing Category entity.
     */
    fun updateEntity(existing: Category, request: CreateOrUpdateCategoryRequest): Category {
        return existing.copy(
            name = request.name,
            description = request.description,
            translationString = request.translationString,
            icon = request.icon
        )
    }

    /**
     * Converts a Category entity to a CategoryResponse DTO.
     */
    fun toResponse(category: Category): CategoryDto {
        return CategoryDto(
            name = category.name,
            description = category.description,
            translationString = category.translationString,
            icon = category.icon
        )
    }
}
