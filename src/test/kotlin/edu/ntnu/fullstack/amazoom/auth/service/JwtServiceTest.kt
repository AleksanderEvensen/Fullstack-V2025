package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.auth.config.AuthProperties
import edu.ntnu.fullstack.amazoom.common.entity.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.GrantedAuthority
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {
    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var authProperties: AuthProperties

    private lateinit var testUser: User

    @BeforeEach
    fun setup() {
        testUser = User(
            id = 1L,
            email = "test.user@example.com",
            password = "password123",
            name = "Test User",
            phoneNumber = "12345678"
        )
    }

    @Test
    fun `test generate and validate token`() {
        val customUserDetails = CustomUserDetails(
            username = testUser.email,
            password = testUser.password,
            authorities = listOf(GrantedAuthority { "ROLE_USER" })
        )

        // Generate token
        val token = jwtService.generateToken(customUserDetails)

        // Validate token
        val validToken = jwtService.isTokenValid(token)

        assertTrue(validToken)
    }

    @Test
    fun `test validate invalid token`() {
        val invalidToken = "invalid.token.here"

        val isValid = jwtService.isTokenValid(invalidToken)

        assertFalse(isValid)
    }

    @Test
    fun `test extract username from token`() {
        val customUserDetails = CustomUserDetails(
            username = testUser.email,
            password = testUser.password,
            authorities = listOf(GrantedAuthority { "ROLE_USER" })
        )

        // Generate token
        val token = jwtService.generateToken(customUserDetails)

        // Extract username
        val username = jwtService.extractUsername(token)

        // Verify username
        assertEquals(testUser.email, username)
    }

    @Test
    fun `test extract username from invalid token`() {
        val invalidToken = "invalid.token.here"

        val username = jwtService.extractUsername(invalidToken)

        assertNull(username)
    }
}