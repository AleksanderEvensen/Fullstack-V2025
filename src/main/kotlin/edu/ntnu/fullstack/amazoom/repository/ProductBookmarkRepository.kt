package edu.ntnu.fullstack.amazoom.repository

import edu.ntnu.fullstack.amazoom.model.ProductBookmark
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductBookmarkRepository : JpaRepository<ProductBookmark, Long> {
    @Modifying
    @Query("DELETE FROM product_bookmark pb WHERE pb.productId = :productId AND pb.userId = :userId")
    fun deleteByProductIdAndUserId(productId: Long, userId: Long): Int
} 