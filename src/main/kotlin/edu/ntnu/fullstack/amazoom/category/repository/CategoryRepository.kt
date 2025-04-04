package edu.ntnu.fullstack.amazoom.category.repository

import edu.ntnu.fullstack.amazoom.category.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long>
