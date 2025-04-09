package edu.ntnu.fullstack.amazoom.category.exception

/**
 * Exception thrown when a requested category cannot be found.
 */
class CategoryNotFoundException(message: String = "Category not found") : RuntimeException(message)