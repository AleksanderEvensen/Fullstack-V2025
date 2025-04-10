package edu.ntnu.fullstack.amazoom.common.mapper

import edu.ntnu.fullstack.amazoom.common.dto.FullUserDto
import edu.ntnu.fullstack.amazoom.common.dto.UserDto
import edu.ntnu.fullstack.amazoom.common.entity.User

object UserMapper {

    fun toDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            name = user.name,
            profileImageUrl = user.profileImageUrl,
            address = user.address?.let { AddressMapper.toDto(it) },
        )
    }

    fun toFullDto(user: User): FullUserDto {
        return FullUserDto(
            id = user.id,
            name = user.name,
            email = user.email,
            phoneNumber = user.phoneNumber,
            profileImageUrl = user.profileImageUrl,
            address = user.address?.let { AddressMapper.toDto(it) },
        )
    }
}