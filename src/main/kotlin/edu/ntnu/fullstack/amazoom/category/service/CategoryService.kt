package edu.ntnu.fullstack.amazoom.category.service

import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequest
import edu.ntnu.fullstack.amazoom.category.dto.CategoryResponse
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.category.mapper.CategoryMapper
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
)  {

    fun createCategory(request: CreateOrUpdateCategoryRequest): CategoryResponse {
        // 1) Convert request -> entity
        val entity: Category = CategoryMapper.toEntity(request)
        // 2) Save
        val saved: Category = categoryRepository.save(entity)
        // 3) Return response
        return CategoryMapper.toResponse(saved)
    }

    fun getCategory(id: Long): CategoryResponse {
        val category = categoryRepository.findById(id)
            .orElseThrow { NoSuchElementException("No Category found with id=$id") }
        return CategoryMapper.toResponse(category)
    }

    fun updateCategory(id: Long, request: CreateOrUpdateCategoryRequest): CategoryResponse {
        val existing = categoryRepository.findById(id)
            .orElseThrow { NoSuchElementException("No Category found with id=$id") }
        val updatedEntity = CategoryMapper.updateEntity(existing, request)
        val savedEntity = categoryRepository.save(updatedEntity)
        return CategoryMapper.toResponse(savedEntity)
    }

    fun deleteCategory(id: Long) {
        // Optional check if Category exists
        if (!categoryRepository.existsById(id)) {
            throw NoSuchElementException("No Category found with id=$id")
        }
        categoryRepository.deleteById(id)
    }

    fun listAllCategories(): List<CategoryResponse> {
        return categoryRepository.findAll().map { CategoryMapper.toResponse(it) }
    }
}
