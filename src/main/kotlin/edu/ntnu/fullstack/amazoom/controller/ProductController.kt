package edu.ntnu.fullstack.amazoom.controller

import edu.ntnu.fullstack.amazoom.model.Product
import edu.ntnu.fullstack.amazoom.model.ProductImage
import edu.ntnu.fullstack.amazoom.model.ProductBookmark
import edu.ntnu.fullstack.amazoom.service.ProductService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing products.
 */
@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    /**
     * Retrieves a list of all products with optional filtering and pagination.
     *
     * @param categoryId optional category ID filter.
     * @param search optional search query.
     * @param page page number for pagination.
     * @param size page size for pagination.
     * @return a ResponseEntity containing a page of products.
     */
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Successfully retrieved products", content = [Content(schema = Schema(implementation = Page::class))])
    fun getAllProducts(
        @RequestParam(required = false) categoryId: Long?,
        @RequestParam(required = false) search: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<Product>> {
        val pageable = PageRequest.of(page, size)
        val products = productService.getAllProducts(categoryId, search, pageable)
        return ResponseEntity.ok(products)
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param id the ID of the product to retrieve.
     * @return a ResponseEntity containing the product, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved product", content = [Content(schema = Schema(implementation = Product::class))])
    @ApiResponse(responseCode = "404", description = "Product not found", content = [Content()])
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        return productService.getProductById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    /**
     * Creates a new product.
     *
     * @param product the details of the product to create.
     * @return a ResponseEntity containing the created product.
     */
    @PostMapping
    @ApiResponse(responseCode = "201", description = "Product created successfully", content = [Content(schema = Schema(implementation = Product::class))])
    @ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()])
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> {
        return ResponseEntity.ok(productService.createProduct(product))
    }

    /**
     * Updates an existing product.
     *
     * @param id the ID of the product to update.
     * @param product the updated product details.
     * @return a ResponseEntity containing the updated product, or a 404 status if not found.
     */
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Product updated successfully", content = [Content(schema = Schema(implementation = Product::class))])
    @ApiResponse(responseCode = "404", description = "Product not found", content = [Content()])
    fun updateProduct(@PathVariable id: Long, @RequestBody product: Product): ResponseEntity<Product> {
        return productService.updateProduct(id, product)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    /**
     * Deletes an existing product.
     *
     * @param id the ID of the product to delete.
     * @return a ResponseEntity with a 204 status if deleted, or a 404 status if not found.
     */
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Product not found", content = [Content()])
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        return if (productService.deleteProduct(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Adds an image to a product.
     *
     * @param id the ID of the product.
     * @param image the image details.
     * @return a ResponseEntity containing the added image.
     */
    @PostMapping("/{id}/images")
    @ApiResponse(responseCode = "201", description = "Image added successfully", content = [Content(schema = Schema(implementation = ProductImage::class))])
    @ApiResponse(responseCode = "404", description = "Product not found", content = [Content()])
    fun addProductImage(@PathVariable id: Long, @RequestBody image: ProductImage): ResponseEntity<ProductImage> {
        return productService.addProductImage(id, image)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    /**
     * Adds a product to user's bookmarks.
     *
     * @param id the ID of the product.
     * @param userId the ID of the user.
     * @return a ResponseEntity containing the product bookmark.
     */
    @PostMapping("/{id}/bookmark")
    @ApiResponse(responseCode = "201", description = "Product bookmarked successfully", content = [Content(schema = Schema(implementation = ProductBookmark::class))])
    @ApiResponse(responseCode = "404", description = "Product not found", content = [Content()])
    fun bookmarkProduct(@PathVariable id: Long, @RequestParam userId: Long): ResponseEntity<ProductBookmark> {
        return productService.bookmarkProduct(id, userId)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    /**
     * Removes a product from user's bookmarks.
     *
     * @param id the ID of the product.
     * @param userId the ID of the user.
     * @return a ResponseEntity with a 204 status if removed, or a 404 status if not found.
     */
    @DeleteMapping("/{id}/bookmark")
    @ApiResponse(responseCode = "204", description = "Bookmark removed successfully", content = [Content()])
    @ApiResponse(responseCode = "404", description = "Bookmark not found", content = [Content()])
    fun removeBookmark(@PathVariable id: Long, @RequestParam userId: Long): ResponseEntity<Void> {
        return if (productService.removeBookmark(id, userId)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}