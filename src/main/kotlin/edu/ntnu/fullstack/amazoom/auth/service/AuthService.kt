package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.auth.dto.AuthResponseDto
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequestDto
import edu.ntnu.fullstack.amazoom.auth.exception.InvalidCredentialsException
import edu.ntnu.fullstack.amazoom.common.dto.CreateUserDto
import edu.ntnu.fullstack.amazoom.common.exception.UserNotFoundException
import edu.ntnu.fullstack.amazoom.common.service.UserService
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Service responsible for user authentication operations.
 * Handles user registration and login processes.
 */
@Service
class AuthService(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: UserDetailsServiceImpl,
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    /**
     * Registers a new user in the system.
     *
     * @param data Registration information for the new user
     * @return Authentication response with JWT token
     */
    @Transactional
    fun register(data: RegisterRequestDto): AuthResponseDto {
        val encodedPassword = passwordEncoder.encode(data.password)

        val user = userService.createUser(
            CreateUserDto(
                firstName = data.firstName,
                lastName = data.lastName,
                email = data.email,
                password = encodedPassword,
                phoneNumber = data.phoneNumber,
            )
        )

        val userDetails = userDetailsService.loadUserByUsername(user.email)
        val accessToken = jwtService.generateToken(userDetails)

        logger.info("Successfully registered user: {}", data.email)

        return AuthResponseDto(
            accessToken = accessToken,
        )
    }

    /**
     * Authenticates a user with their email and password.
     *
     * @param email The user's email address
     * @param password The user's password
     * @return Authentication response with JWT token
     * @throws InvalidCredentialsException if credentials are invalid
     * @throws UserNotFoundException if the user doesn't exist
     */
    @Transactional
    fun login(email: String, password: String): AuthResponseDto {
        val user = userService.getUserByEmail(email)

        val passwordMatches = passwordEncoder.matches(password, user.password)

        if (!passwordMatches) {
            logger.warn("Failed login attempt for user: {} - Invalid password", email)
            throw InvalidCredentialsException()
        }

        val userDetails = userDetailsService.loadUserByUsername(email)
        val accessToken = jwtService.generateToken(userDetails)

        logger.info("Successful login for user: {}", email)

        return AuthResponseDto(
            accessToken = accessToken,
        )
    }
}