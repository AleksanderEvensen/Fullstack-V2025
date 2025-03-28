package edu.ntnu.fullstack.amazoom.service.impl

import edu.ntnu.fullstack.amazoom.model.Product
import edu.ntnu.fullstack.amazoom.model.ProductImage
import edu.ntnu.fullstack.amazoom.model.ProductBookmark
import edu.ntnu.fullstack.amazoom.repository.ProductRepository
import edu.ntnu.fullstack.amazoom.repository.ProductImageRepository
import edu.ntnu.fullstack.amazoom.repository.ProductBookmarkRepository
import edu.ntnu.fullstack.amazoom.service.ProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val productBookmarkRepository: ProductBookmarkRepository
) : ProductService {

    @Transactional(readOnly = true)
    override fun getAllProducts(
        categoryId: Long?,
        search: String?,
        pageable: Pageable
    ): Page<Product> {
        return when {
            categoryId != null && search != null -> 
                productRepository.findByCategoryIdAndTitleContainingOrDescriptionContaining(
                    categoryId, search, search, pageable
                )
            categoryId != null -> 
                productRepository.findByCategoryId(categoryId, pageable)
            search != null -> 
                productRepository.findByTitleContainingOrDescriptionContaining(search, search, pageable)
            else -> 
                productRepository.findAll(pageable)
        }
    }

    @Transactional(readOnly = true)
    override fun getProductById(id: Long): Product? {
        return productRepository.findById(id).orElse(null)
    }

    @Transactional
    override fun createProduct(product: Product): Product {
        return productRepository.save(product)
    }

    @Transactional
    override fun updateProduct(id: Long, product: Product): Product? {
        val existingProduct = productRepository.findById(id).orElse(null) ?: return null
        return productRepository.save(product.copy(id = existingProduct.id))
    }

    @Transactional
    override fun deleteProduct(id: Long): Boolean {
        return if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    @Transactional
    override fun addProductImage(productId: Long, image: ProductImage): ProductImage? {
        if (!productRepository.existsById(productId)) return null
        return productImageRepository.save(image.copy(productId = productId))
    }

    @Transactional
    override fun bookmarkProduct(productId: Long, userId: Long): ProductBookmark? {
        if (!productRepository.existsById(productId)) return null
        return productBookmarkRepository.save(ProductBookmark(productId = productId, userId = userId))
    }

    @Transactional
    override fun removeBookmark(productId: Long, userId: Long): Boolean {
        return productBookmarkRepository.deleteByProductIdAndUserId(productId, userId) > 0
    }
} 