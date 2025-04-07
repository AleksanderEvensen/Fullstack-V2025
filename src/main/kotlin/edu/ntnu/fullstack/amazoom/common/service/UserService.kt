package edu.ntnu.fullstack.amazoom.common.service

import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.exception.MissingRoleException
import edu.ntnu.fullstack.amazoom.common.repository.RoleRepository
import edu.ntnu.fullstack.amazoom.auth.service.UserDetailsImpl
import edu.ntnu.fullstack.amazoom.common.dto.CreateUserDto
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.exception.UserAlreadyExistsException
import edu.ntnu.fullstack.amazoom.common.exception.UserNotFoundException
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
) {

    @Transactional
    fun createUser(data: CreateUserDto): User {
        if(userRepository.existsByEmail(data.email)) {
            throw UserAlreadyExistsException("User with email ${data.email} already exists")
        }

        if(userRepository.existsByPhoneNumber(data.phoneNumber)) {
            throw UserAlreadyExistsException("User with phone number ${data.phoneNumber} already exists")
        }

        val userRole = roleRepository.findByName(RoleName.ROLE_USER) ?: throw MissingRoleException()

        val user = User(
            firstName = data.firstName,
            lastName = data.lastName,
            email = data.email,
            phoneNumber = data.phoneNumber,
            password = data.password,
            roles = mutableSetOf(userRole),
            address = null,
        )

        return userRepository.save(user)
    }

    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal
        return if (principal is UserDetailsImpl) {
            principal.getDomainUser()
        } else {
            throw UserNotFoundException()
        }
    }

    fun getUserById(id: UUID): User {
        val user = userRepository.findById(id).orElseThrow {
            UserNotFoundException("User with id $id not found")
        }

        return user
    }

    fun getCurrentAuthenticatedUser(): Optional<UserDetailsImpl> {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal
        return if (principal is UserDetailsImpl) {
            Optional.of(principal)
        } else {
            Optional.empty()
        }

    }
}