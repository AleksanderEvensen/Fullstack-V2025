package edu.ntnu.fullstack.amazoom.repository

import edu.ntnu.fullstack.amazoom.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByCategoryId(categoryId: Long, pageable: Pageable): Page<Product>
    
    @Query("SELECT p FROM product p WHERE p.title LIKE %:search% OR p.briefDescription LIKE %:search% OR p.fullDescription LIKE %:search%")
    fun findByTitleContainingOrDescriptionContaining(search: String, search2: String, pageable: Pageable): Page<Product>
    
    @Query("SELECT p FROM product p WHERE p.categoryId = :categoryId AND (p.title LIKE %:search% OR p.briefDescription LIKE %:search% OR p.fullDescription LIKE %:search%)")
    fun findByCategoryIdAndTitleContainingOrDescriptionContaining(
        categoryId: Long,
        search: String,
        search2: String,
        pageable: Pageable
    ): Page<Product>
} 