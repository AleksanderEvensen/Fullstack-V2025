package edu.ntnu.fullstack.amazoom.category.controller

import edu.ntnu.fullstack.amazoom.category.dto.CategoryDto
import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequestDto
import edu.ntnu.fullstack.amazoom.category.service.CategoryService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing product categories.
 * Provides endpoints for CRUD operations on categories.
 */
@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    private val logger = LoggerFactory.getLogger(CategoryController::class.java)

    /**
     * Creates a new category.
     * Only accessible to administrators.
     *
     * @param request The request with category details
     * @return The created category
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@Valid @RequestBody request: CreateOrUpdateCategoryRequestDto): ResponseEntity<CategoryDto> {
        logger.debug("REST request to create category: {}", request.name)

        val createdCategory = categoryService.createCategory(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory)
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve
     * @return The category
     */
    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: Long): ResponseEntity<CategoryDto> {
        logger.debug("REST request to get category: {}", id)

        val category = categoryService.getCategory(id)
        return ResponseEntity.ok(category)
    }

    /**
     * Updates an existing category.
     * Only accessible to administrators.
     *
     * @param id The ID of the category to update
     * @param request The request with updated details
     * @return The updated category
     */
    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateOrUpdateCategoryRequestDto
    ): ResponseEntity<CategoryDto> {
        logger.debug("REST request to update category: {}", id)

        val updatedCategory = categoryService.updateCategory(id, request)
        return ResponseEntity.ok(updatedCategory)
    }

    /**
     * Deletes a category by its ID.
     * Only accessible to administrators.
     *
     * @param id The ID of the category to delete
     * @return No content response
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
        logger.debug("REST request to delete category: {}", id)

        categoryService.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Lists all categories.
     *
     * @return A list of all categories
     */
    @GetMapping
    fun listAllCategories(): ResponseEntity<List<CategoryDto>> {
        logger.debug("REST request to get all categories")

        val categories = categoryService.listAllCategories()
        return ResponseEntity.ok(categories)
    }
}