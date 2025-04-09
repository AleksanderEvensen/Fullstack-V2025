package edu.ntnu.fullstack.amazoom.common.service

import edu.ntnu.fullstack.amazoom.common.dto.AddressDto
import edu.ntnu.fullstack.amazoom.common.repository.RoleRepository
import edu.ntnu.fullstack.amazoom.common.dto.CreateUserDto
import edu.ntnu.fullstack.amazoom.common.dto.FullUserDto
import edu.ntnu.fullstack.amazoom.common.entity.Address
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.exception.MissingRoleException
import edu.ntnu.fullstack.amazoom.common.exception.UserAlreadyExistsException
import edu.ntnu.fullstack.amazoom.common.exception.UserNotFoundException
import edu.ntnu.fullstack.amazoom.common.mapper.UserMapper
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

/**
 * Service for managing user operations.
 * Handles user creation, retrieval, and profile management.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    /**
     * Creates a new user in the system.
     *
     * @param data The data for the new user
     * @return The created user entity
     * @throws UserAlreadyExistsException if a user with the email or phone number already exists
     * @throws MissingRoleException if the USER role doesn't exist in the database
     */
    fun createUser(data: CreateUserDto): User {
        if (userRepository.existsByEmail(data.email)) {
            logger.warn("Failed to create user: Email {} already exists", data.email)
            throw UserAlreadyExistsException("User with email ${data.email} already exists")
        }

        if (userRepository.existsByPhoneNumber(data.phoneNumber)) {
            logger.warn("Failed to create user: Phone number {} already exists", data.phoneNumber)
            throw UserAlreadyExistsException("User with phone number ${data.phoneNumber} already exists")
        }

        val userRole = roleRepository.findByName(RoleName.ROLE_USER)
            ?: throw MissingRoleException("ROLE_USER not found in database")

        val user = User(
            firstName = data.firstName,
            lastName = data.lastName,
            email = data.email,
            phoneNumber = data.phoneNumber,
            password = data.password,
            address = null,
            roles = mutableSetOf(userRole)
        )

        val savedUser = userRepository.save(user)
        logger.info("Successfully created user with ID: {}", savedUser.id)
        return savedUser
    }

    /**
     * Updates the password for the currently authenticated user.
     *
     * @param password The new password to set
     */
    fun updatePassword(password: String){
        val user = getCurrentUser()
        user.password = password
        userRepository.save(user)
        logger.info("Password updated for user: {}", user.email)
    }

    /**
     * Gets the profile of the currently authenticated user.
     *
     * @return The full user profile DTO
     * @throws UserNotFoundException if the authenticated user doesn't exist
     */
    fun getProfile(): FullUserDto {
        val currentUser = getCurrentUser()
        return UserMapper.toFullDto(currentUser)
    }

    /**
     * Gets the user entity for the currently authenticated user.
     *
     * @return The current user entity
     * @throws UserNotFoundException if the authenticated user doesn't exist
     */
    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name

        return userRepository.findByEmail(email).orElseThrow {
            logger.warn("User not found with email: {}", email)
            UserNotFoundException("User with email $email not found")
        }
    }

    /**
     * Gets a user by their ID.
     *
     * @param id The ID of the user to retrieve
     * @return The user entity
     * @throws UserNotFoundException if no user exists with the given ID
     */
    fun getUserById(id: Long): User {
        val user = userRepository.findById(id).orElseThrow {
            logger.warn("User not found with ID: {}", id)
            UserNotFoundException("User with id $id not found")
        }

        return user
    }

    /**
     * Gets a user by their email address.
     *
     * @param email The email of the user to retrieve
     * @return The user entity
     * @throws UserNotFoundException if no user exists with the given email
     */
    fun getUserByEmail(email: String): User {
        val user = userRepository.findByEmail(email).orElseThrow {
            logger.warn("User not found with email: {}", email)
            UserNotFoundException("User with email $email not found")
        }

        return user
    }

    /**
     * Updates the user's profile image.
     *
     * @param imageUrl The URL or filename of the uploaded image
     */
    @Transactional
    fun updateProfileImage(imageUrl: String) {
        val user = getCurrentUser()

        // Update the user's profile image URL
        user.profileImageUrl = imageUrl

        userRepository.save(user)
        logger.info("Profile image updated for user: {}", user.email)
    }

    /**
     * Updates the user's address information.
     *
     * @param address The new address information
     */
    @Transactional
    fun updateAddress(address: AddressDto) {
        val user = getCurrentUser()

        // Convert DTO to entity and update the user's address
        val addressEntity = Address(
            streetName = address.streetName,
            streetNumber = address.streetNumber,
            postalCode = address.postalCode,
            city = address.city,
            country = address.country
        )

        user.address = addressEntity

        userRepository.save(user)
        logger.info("Address updated for user: {}", user.email)
    }


    /**
     * Gets a user by their National ID Number (used by Vipps)
     *
     * @param nin The National ID Number to look up
     * @return The user entity
     * @throws UserNotFoundException if no user exists with the given NIN
     */
    fun getUserByNin(nin: String): User {
        return userRepository.findByNin(nin).orElseThrow {
            logger.warn("User not found with nin: $nin")
            UserNotFoundException("User with NIN $nin not found")
        }
    }
}