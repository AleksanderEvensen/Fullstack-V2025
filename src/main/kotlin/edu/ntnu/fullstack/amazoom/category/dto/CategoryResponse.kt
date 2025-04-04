package edu.ntnu.fullstack.amazoom.category.dto

data class CategoryResponse(
    val id: Long,
    val name: String,
    val description: String,
    val translationString: String,
    val icon: String
)
