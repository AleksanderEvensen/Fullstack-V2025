package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.auth.dto.LoginRequestDto
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequestDto
import edu.ntnu.fullstack.amazoom.auth.exception.InvalidCredentialsException
import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.exception.UserAlreadyExistsException
import edu.ntnu.fullstack.amazoom.common.exception.UserNotFoundException
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceIntegrationTest {
    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private val testEmail = "test.user@example.com"
    private val testPassword = "password123"
    private val testName = "Test User"
    private val testPhoneNumber = "12345678"

    @BeforeEach
    fun setup() {
        // Clean up any existing test users
        userRepository.deleteAll()
    }

    @Test
    @Transactional()
    fun `test register new user`() {
        // Create registration request
        val request = RegisterRequestDto(
            email = testEmail,
            password = testPassword,
            name = testName,
            phoneNumber = testPhoneNumber,
            postalCode = "12345",
            city = "Test City",
            street = "Test Street",
        )

        val response = authService.register(request)
        assertNotNull(response.accessToken)
        val savedUser = userRepository.findByEmail(testEmail)
        assertNotNull(savedUser)

    }

    @Test
    fun `test register with existing email throws exception`() {
        // Create and save a user first
        userRepository.save(
            User(
                email = testEmail,
                password = passwordEncoder.encode(testPassword),
                name = testName,
                phoneNumber = testPhoneNumber
            )
        )

        // Try to register with same email
        val request = RegisterRequestDto(
            email = testEmail,
            password = testPassword,
            name = testName,
            phoneNumber = testPhoneNumber,
            postalCode = "12345",
            city = "Test City",
            street = "Test Street",
        )

        assertThrows(UserAlreadyExistsException::class.java) {
            authService.register(request)
        }
    }

    @Test
    fun `test login with valid credentials`() {
        // Create and save a user
        userRepository.save(
            User(
                email = testEmail,
                password = passwordEncoder.encode(testPassword),
                name = testName,
                phoneNumber = testPhoneNumber
            )
        )

        // Create login request
        val request = LoginRequestDto(
            email = testEmail,
            password = testPassword
        )

        // Login
        val response = authService.login(request.email, request.password)

        // Verify response
        assertNotNull(response.accessToken)

    }

    @Test
    fun `test login with invalid password throws exception`() {
        // Create and save a user
        userRepository.save(
            User(
                email = testEmail,
                password = passwordEncoder.encode(testPassword),
                name = testName,
                phoneNumber = testPhoneNumber
            )
        )

        // Create login request with wrong password
        val request = LoginRequestDto(
            email = testEmail,
            password = "wrongpassword"
        )

        assertThrows(InvalidCredentialsException::class.java) {
            authService.login(request.email, request.password)
        }
    }

    @Test
    fun `test login with non-existent email throws exception`() {
        // Create login request for non-existent user
        val request = LoginRequestDto(
            email = "nonexistent@example.com",
            password = testPassword
        )

        assertThrows(UserNotFoundException::class.java) {
            authService.login(request.email, request.password)
        }
    }
} 