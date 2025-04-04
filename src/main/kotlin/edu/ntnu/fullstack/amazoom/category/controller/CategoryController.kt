package edu.ntnu.fullstack.amazoom.category.controller

import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequest
import edu.ntnu.fullstack.amazoom.category.dto.CategoryResponse
import edu.ntnu.fullstack.amazoom.category.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing categories.
 */
@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    /**
     * Creates a new category.
     *
     * @param request the request containing the details of the category to create
     * @return the created category
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@RequestBody request: CreateOrUpdateCategoryRequest): CategoryResponse {
        return categoryService.createCategory(request)
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to retrieve
     * @return the retrieved category
     */
    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: Long): CategoryResponse {
        return categoryService.getCategory(id)
    }

    /**
     * Updates an existing category.
     *
     * @param id the ID of the category to update
     * @param request the request containing the updated details of the category
     * @return the updated category
     */
    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: Long,
        @RequestBody request: CreateOrUpdateCategoryRequest
    ): CategoryResponse {
        return categoryService.updateCategory(id, request)
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCategory(@PathVariable id: Long) {
        categoryService.deleteCategory(id)
    }

    /**
     * Lists all categories.
     *
     * @return a list of all categories
     */
    @GetMapping
    fun listAllCategories(): List<CategoryResponse> {
        return categoryService.listAllCategories()
    }
}