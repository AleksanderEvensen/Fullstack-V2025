package edu.ntnu.fullstack.amazoom.service.impl

import edu.ntnu.fullstack.amazoom.model.Category
import edu.ntnu.fullstack.amazoom.repository.CategoryRepository
import edu.ntnu.fullstack.amazoom.service.CategoryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {

    @Transactional(readOnly = true)
    override fun getAllCategories(): List<Category> {
        return categoryRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun getCategoryById(id: Long): Category? {
        return categoryRepository.findById(id).orElse(null)
    }

    @Transactional
    override fun createCategory(category: Category): Category {
        return categoryRepository.save(category)
    }

    @Transactional
    override fun updateCategory(id: Long, category: Category): Category? {
        val existingCategory = categoryRepository.findById(id).orElse(null) ?: return null
        return categoryRepository.save(category.copy(id = existingCategory.id))
    }

    @Transactional
    override fun deleteCategory(id: Long): Boolean {
        return if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id)
            true
        } else {
            false
        }
    }
} 