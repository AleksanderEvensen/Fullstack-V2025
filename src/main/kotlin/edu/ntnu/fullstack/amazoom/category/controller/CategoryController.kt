package edu.ntnu.fullstack.amazoom.category.controller

import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequest
import edu.ntnu.fullstack.amazoom.category.dto.CategoryResponse
import edu.ntnu.fullstack.amazoom.category.service.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun createCategory(@RequestBody request: CreateOrUpdateCategoryRequest): ResponseEntity<CategoryResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(categoryService.createCategory(request))
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to retrieve
     * @return the retrieved category
     */
    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: Long): ResponseEntity<CategoryResponse> {
        return ResponseEntity.ok(categoryService.getCategory(id))
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
    ): ResponseEntity<CategoryResponse> {
        return ResponseEntity.ok(categoryService.updateCategory(id, request))
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
        categoryService.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }

    /**
     * Lists all categories.
     *
     * @return a list of all categories
     */
    @GetMapping
    fun listAllCategories(): ResponseEntity<List<CategoryResponse>> {
        return ResponseEntity.ok(categoryService.listAllCategories())
    }
}