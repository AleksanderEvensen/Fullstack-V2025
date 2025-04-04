package edu.ntnu.fullstack.amazoom.category.dto

data class CreateOrUpdateCategoryRequest(
    val name: String,
    val description: String,
    val translationString: String,
    val icon: String
)
