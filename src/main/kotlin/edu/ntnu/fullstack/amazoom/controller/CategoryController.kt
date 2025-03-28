package edu.ntnu.fullstack.amazoom.controller

import edu.ntnu.fullstack.amazoom.model.Category
import edu.ntnu.fullstack.amazoom.service.CategoryService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
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
     * Retrieves a list of all categories.
     *
     * @return a ResponseEntity containing a list of all categories.
     */
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Successfully retrieved categories", content = [Content(schema = Schema(implementation = List::class))])
    fun getAllCategories(): ResponseEntity<List<Category>> {
        return ResponseEntity.ok(categoryService.getAllCategories())
    }

    /**
     * Retrieves a specific category by its ID.
     *
     * @param id the ID of the category to retrieve.
     * @return a ResponseEntity containing the category, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved category", content = [Content(schema = Schema(implementation = Category::class))])
    @ApiResponse(responseCode = "404", description = "Category not found", content = [Content()])
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<Category> {
        return categoryService.getCategoryById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    /**
     * Creates a new category.
     *
     * @param category the details of the category to create.
     * @return a ResponseEntity containing the created category.
     */
    @PostMapping
    @ApiResponse(responseCode = "201", description = "Category created successfully", content = [Content(schema = Schema(implementation = Category::class))])
    @ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()])
    fun createCategory(@RequestBody category: Category): ResponseEntity<Category> {
        return ResponseEntity.ok(categoryService.createCategory(category))
    }

    /**
     * Updates an existing category.
     *
     * @param id the ID of the category to update.
     * @param category the updated category details.
     * @return a ResponseEntity containing the updated category, or a 404 status if not found.
     */
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Category updated successfully", content = [Content(schema = Schema(implementation = Category::class))])
    @ApiResponse(responseCode = "404", description = "Category not found", content = [Content()])
    fun updateCategory(@PathVariable id: Long, @RequestBody category: Category): ResponseEntity<Category> {
        return categoryService.updateCategory(id, category)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    /**
     * Deletes an existing category.
     *
     * @param id the ID of the category to delete.
     * @return a ResponseEntity with a 204 status if deleted, or a 404 status if not found.
     */
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Category not found", content = [Content()])
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
        return if (categoryService.deleteCategory(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}