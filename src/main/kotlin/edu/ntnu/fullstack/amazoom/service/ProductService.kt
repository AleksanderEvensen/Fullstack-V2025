package edu.ntnu.fullstack.amazoom.service

import edu.ntnu.fullstack.amazoom.model.Product
import edu.ntnu.fullstack.amazoom.model.ProductImage
import edu.ntnu.fullstack.amazoom.model.ProductBookmark
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductService {
    fun getAllProducts(
        categoryId: Long?,
        search: String?,
        pageable: Pageable
    ): Page<Product>
    
    fun getProductById(id: Long): Product?
    fun createProduct(product: Product): Product
    fun updateProduct(id: Long, product: Product): Product?
    fun deleteProduct(id: Long): Boolean
    
    fun addProductImage(productId: Long, image: ProductImage): ProductImage?
    fun bookmarkProduct(productId: Long, userId: Long): ProductBookmark?
    fun removeBookmark(productId: Long, userId: Long): Boolean
} 