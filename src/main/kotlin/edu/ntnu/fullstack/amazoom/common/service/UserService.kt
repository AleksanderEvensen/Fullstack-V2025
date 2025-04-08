package edu.ntnu.fullstack.amazoom.common.service

import edu.ntnu.fullstack.amazoom.auth.service.CustomUserDetails
import edu.ntnu.fullstack.amazoom.common.repository.RoleRepository
import edu.ntnu.fullstack.amazoom.common.dto.CreateUserDto
import edu.ntnu.fullstack.amazoom.common.dto.FullUserDto
import edu.ntnu.fullstack.amazoom.common.dto.UserDto
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.exception.UserAlreadyExistsException
import edu.ntnu.fullstack.amazoom.common.exception.UserNotFoundException
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
) {

    fun createUser(data: CreateUserDto): User {
        if(userRepository.existsByEmail(data.email)) {
            throw UserAlreadyExistsException("User with email ${data.email} already exists")
        }

        if(userRepository.existsByPhoneNumber(data.phoneNumber)) {
            throw UserAlreadyExistsException("User with phone number ${data.phoneNumber} already exists")
        }

        val userRole = roleRepository.findByName(RoleName.ROLE_USER) ?:
            throw UserNotFoundException("Role not found")


        val user = User(
            firstName = data.firstName,
            lastName = data.lastName,
            email = data.email,
            phoneNumber = data.phoneNumber,
            password = data.password,
            address = null,
            roles = mutableSetOf(userRole)
        )

        return userRepository.save(user)
    }

    fun getProfile(): FullUserDto {
        val currentUser = getCurrentUser()

        return currentUser.toFullDto()
    }

    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name
        return userRepository.findByEmail(email).orElseThrow{
            UserNotFoundException("User with email $email not found")
        }
    }

    fun getUserById(id: Long): User {
        val user = userRepository.findById(id).orElseThrow {
            UserNotFoundException("User with id $id not found")
        }

        return user
    }

    fun getUserByEmail(email: String): User {
        val user = userRepository.findByEmail(email).orElseThrow {
            UserNotFoundException("User with email $email not found")
        }

        return user
    }
}