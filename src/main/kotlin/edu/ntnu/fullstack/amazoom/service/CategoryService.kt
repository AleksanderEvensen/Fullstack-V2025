package edu.ntnu.fullstack.amazoom.service

import edu.ntnu.fullstack.amazoom.model.Category

interface CategoryService {
    fun getAllCategories(): List<Category>
    fun getCategoryById(id: Long): Category?
    fun createCategory(category: Category): Category
    fun updateCategory(id: Long, category: Category): Category?
    fun deleteCategory(id: Long): Boolean
} 