package edu.ntnu.fullstack.amazoom.auth.mapper

import edu.ntnu.fullstack.amazoom.auth.dto.UserDto
import edu.ntnu.fullstack.amazoom.auth.entity.User

object UserMapper {

    /**
     * Converts a Category entity to a CategoryResponse DTO.
     */
    fun toDto(user: User): UserDto {
        return UserDto(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            address = user.address,
            profileImageUrl = user.profileImageUrl,
            id = user.id
        )
    }
}