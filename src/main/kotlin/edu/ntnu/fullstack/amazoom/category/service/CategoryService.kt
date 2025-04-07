package edu.ntnu.fullstack.amazoom.category.service

import edu.ntnu.fullstack.amazoom.category.dto.CreateOrUpdateCategoryRequest
import edu.ntnu.fullstack.amazoom.category.dto.CategoryDto
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.category.exception.CategoryNotFoundException
import edu.ntnu.fullstack.amazoom.category.mapper.CategoryMapper
import edu.ntnu.fullstack.amazoom.category.repository.CategoryRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
)  {

    @PreAuthorize("hasRole('ADMIN')")
    fun createCategory(request: CreateOrUpdateCategoryRequest): CategoryDto {
        val entity: Category = CategoryMapper.toEntity(request)
        val saved = categoryRepository.save(entity)
        return saved.toDto()
    }

    fun getCategory(id: Long): CategoryDto {
        val category = categoryRepository.findById(id)
            .orElseThrow { CategoryNotFoundException("No Category found with id=$id") }
        return category.toDto()
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun updateCategory(id: Long, request: CreateOrUpdateCategoryRequest): CategoryDto {
        val existing = categoryRepository.findById(id)
            .orElseThrow { CategoryNotFoundException("No Category found with id=$id") }
        val updatedEntity = CategoryMapper.updateEntity(existing, request)
        val savedEntity = categoryRepository.save(updatedEntity)
        return savedEntity.toDto()
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun deleteCategory(id: Long) {
        if (!categoryRepository.existsById(id)) {
            throw CategoryNotFoundException("No Category found with id=$id")
        }
        categoryRepository.deleteById(id)
    }

    fun listAllCategories(): List<CategoryDto> {
        return categoryRepository.findAll().map { it.toDto() }
    }
}
