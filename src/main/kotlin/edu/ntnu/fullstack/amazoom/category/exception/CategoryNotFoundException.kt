package edu.ntnu.fullstack.amazoom.category.exception

class CategoryNotFoundException(message: String = "Category not found") : RuntimeException(message);