package edu.ntnu.fullstack.amazoom.category.service

import edu.ntnu.fullstack.amazoom.category.dto.CategoryDto
import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequestDto
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.category.exception.CategoryNotFoundException
import edu.ntnu.fullstack.amazoom.category.mapper.CategoryMapper
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

/**
 * Service for managing product categories.
 * Handles CRUD operations for categories.
 */
@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
)  {
    private val logger = LoggerFactory.getLogger(CategoryService::class.java)

    /**
     * Creates a new category.
     * Only administrators can create categories.
     *
     * @param request The request DTO with category details
     * @return The created category as a DTO
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    fun createCategory(request: CreateOrUpdateCategoryRequestDto): CategoryDto {
        val entity: Category = CategoryMapper.toEntity(request)
        val saved = categoryRepository.save(entity)

        logger.info("Created category with ID: {}", saved.id)
        return CategoryMapper.toDto(saved)
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve
     * @return The category as a DTO
     * @throws CategoryNotFoundException if the category does not exist
     */
    fun getCategory(id: Long): CategoryDto {
        val category = categoryRepository.findById(id)
            .orElseThrow { CategoryNotFoundException("No Category found with id=$id") }

        return CategoryMapper.toDto(category)
    }

    /**
     * Updates an existing category.
     * Only administrators can update categories.
     *
     * @param id The ID of the category to update
     * @param request The request DTO with updated details
     * @return The updated category as a DTO
     * @throws CategoryNotFoundException if the category does not exist
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    fun updateCategory(id: Long, request: CreateOrUpdateCategoryRequestDto): CategoryDto {
        val existing = categoryRepository.findById(id)
            .orElseThrow { CategoryNotFoundException("No Category found with id=$id") }

        val updatedEntity = CategoryMapper.updateEntity(existing, request)
        val savedEntity = categoryRepository.save(updatedEntity)

        logger.info("Updated category with ID: {}", savedEntity.id)
        return CategoryMapper.toDto(savedEntity)
    }

    /**
     * Deletes a category by its ID.
     * Only administrators can delete categories.
     *
     * @param id The ID of the category to delete
     * @throws CategoryNotFoundException if the category does not exist
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteCategory(id: Long) {
        if (!categoryRepository.existsById(id)) {
            throw CategoryNotFoundException("No Category found with id=$id")
        }

        categoryRepository.deleteById(id)
        logger.info("Deleted category with ID: {}", id)
    }

    /**
     * Lists all categories.
     *
     * @return A list of all categories as DTOs
     */
    fun listAllCategories(): List<CategoryDto> {
        logger.debug("Getting all categories")
        return categoryRepository.findAll().map { CategoryMapper.toDto(it) }
    }
}