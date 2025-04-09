package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.auth.dto.AuthResponseDto
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequestDto
import edu.ntnu.fullstack.amazoom.auth.exception.InvalidCredentialsException
import edu.ntnu.fullstack.amazoom.common.dto.CreateUserDto
import edu.ntnu.fullstack.amazoom.common.exception.UserNotFoundException
import edu.ntnu.fullstack.amazoom.common.service.UserService
import jakarta.servlet.http.Cookie
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.boot.web.server.Cookie.SameSite
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

        if (user.password == null) {
            logger.warn("User $email doesn't have a password set");
            throw InvalidCredentialsException()
        }
        val passwordMatches = passwordEncoder.matches(password, user.password)

        if (!passwordMatches) {
            logger.warn("Failed login attempt for user: {} - Invalid password", email)
            throw InvalidCredentialsException()
        }

        return AuthResponseDto(
            accessToken = generateAccessTokenForUser(user.email)
        );
    }

    /**
     * Generates an access token for a user.
     *
     * @param email The user's email to generate the token for
     * @return Authentication response with JWT token
     * @throws UserNotFoundException if the user is not found in the database
     */
    @Transactional
    fun generateAccessTokenForUser(email: String): String {
        val userDetails = kotlin.runCatching { userDetailsService.loadUserByUsername(email) }.fold(
            onSuccess = { it },
            onFailure = { throw UserNotFoundException("User with email $email not found") }
        )
        val accessToken = jwtService.generateToken(userDetails);
        logger.info("Successfully generated token for user: $email")

        return accessToken;
    }

    /**
     * Generates a authantication cookie given a access token.
     *
     */
    fun createAuthCookie(accessToken: String): Cookie {
        val cookie = Cookie("am_session", accessToken);
        cookie.apply {
            maxAge = 10 * 60 // 10 minutes
            path = "/"
        }
        return cookie;
    }
    /**
     * Updates a user's password.
     *
     * @param currentPassword The user's current password
     * @param newPassword The new password to set
     * @return Authentication response with new JWT token
     * @throws InvalidCredentialsException if the current password is incorrect
     */
    @Transactional
    fun updatePassword(currentPassword: String, newPassword: String): AuthResponseDto {
        val user = userService.getCurrentUser()

        if (!passwordEncoder.matches(currentPassword, user.password)) {
            logger.warn("Failed password update for user: {} - Current password invalid", user.email)
            throw InvalidCredentialsException("Current password is incorrect")
        }

        val encodedPassword = passwordEncoder.encode(newPassword)
        userService.updatePassword(encodedPassword)

        val userDetails = userDetailsService.loadUserByUsername(user.email)
        val accessToken = jwtService.generateToken(userDetails)

        logger.info("Password updated successfully for user: {}", user.email)

        return AuthResponseDto(
            accessToken = accessToken,
            message = "Password updated successfully"
        )
    }
}