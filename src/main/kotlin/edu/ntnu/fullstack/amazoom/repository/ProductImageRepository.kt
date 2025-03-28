package edu.ntnu.fullstack.amazoom.repository

import edu.ntnu.fullstack.amazoom.model.ProductImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductImageRepository : JpaRepository<ProductImage, Long> 