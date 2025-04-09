package edu.ntnu.fullstack.amazoom.category.controller

import edu.ntnu.fullstack.amazoom.category.dto.CategoryDto
import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequestDto
import edu.ntnu.fullstack.amazoom.category.service.CategoryService
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Categories", description = "Operations for managing product categories")
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
    @Operation(
        summary = "Create a category",
        description = "Creates a new product category. Only accessible to administrators."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "Category created successfully",
            content = [Content(schema = Schema(implementation = CategoryDto::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "403",
            description = "Access denied - requires admin role",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Get a category by ID",
        description = "Retrieves a category by its unique identifier"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Category found",
            content = [Content(schema = Schema(implementation = CategoryDto::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Update a category",
        description = "Updates an existing category by its ID. Only accessible to administrators."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Category updated successfully",
            content = [Content(schema = Schema(implementation = CategoryDto::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "403",
            description = "Access denied - requires admin role",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Delete a category",
        description = "Deletes a category by its ID. Only accessible to administrators."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "204",
            description = "Category deleted successfully"
        ),
        ApiResponse(
            responseCode = "403",
            description = "Access denied - requires admin role",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Category not found",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "List all categories",
        description = "Retrieves a list of all product categories"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "List of categories retrieved successfully",
            content = [Content(array = ArraySchema(schema = Schema(implementation = CategoryDto::class)))]
        )
    )
    @GetMapping
    fun listAllCategories(): ResponseEntity<List<CategoryDto>> {
        logger.debug("REST request to get all categories")

        val categories = categoryService.listAllCategories()
        return ResponseEntity.ok(categories)
    }
}