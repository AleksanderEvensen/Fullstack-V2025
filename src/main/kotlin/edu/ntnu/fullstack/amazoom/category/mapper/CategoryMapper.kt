package edu.ntnu.fullstack.amazoom.category.mapper

import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequestDto
import edu.ntnu.fullstack.amazoom.category.dto.CategoryDto
import edu.ntnu.fullstack.amazoom.category.entity.Category

/**
 * Mapper class for converting between Category entities and DTOs.
 */
object CategoryMapper {

    /**
     * Converts a CreateOrUpdateCategoryRequestDto to a Category entity.
     * Used when creating a new category.
     *
     * @param request The request DTO with category details
     * @return A new Category entity
     */
    fun toEntity(request: CreateOrUpdateCategoryRequestDto): Category {
        return Category(
            name = request.name,
            description = request.description,
            translationString = request.translationString,
            icon = request.icon
        )
    }

    /**
     * Updates an existing Category entity with values from a CreateOrUpdateCategoryRequestDto.
     * Used when updating a category.
     *
     * @param existing The existing Category entity to update
     * @param request The request DTO with updated values
     * @return The updated Category entity
     */
    fun updateEntity(existing: Category, request: CreateOrUpdateCategoryRequestDto): Category {
        return existing.copy(
            name = request.name,
            description = request.description,
            translationString = request.translationString,
            icon = request.icon
        )
    }

    /**
     * Converts a Category entity to a CategoryDto.
     *
     * @param category The Category entity to convert
     * @return The resulting CategoryDto
     */
    fun toDto(category: Category): CategoryDto {
        return CategoryDto(
            id = category.id,
            name = category.name,
            description = category.description,
            translationString = category.translationString,
            icon = category.icon
        )
    }
}
