package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserDetailsServiceTest {
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var userRepository: UserRepository

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
    fun `test load user by username`() {
        // Create and save a user
        val user = userRepository.save(
            User(
                email = testEmail,
                password = testPassword,
                name = testName,
                phoneNumber = testPhoneNumber
            )
        )

        // Load user details
        val userDetails = userDetailsService.loadUserByUsername(testEmail)

        // Verify user details
        assertEquals(testEmail, userDetails.username)
        assertEquals(testPassword, userDetails.password)
        assertTrue(userDetails.isEnabled)
        assertTrue(userDetails.isAccountNonExpired)
        assertTrue(userDetails.isAccountNonLocked)
        assertTrue(userDetails.isCredentialsNonExpired)
    }

    @Test
    fun `test load non-existent user throws exception`() {
        assertThrows(UsernameNotFoundException::class.java) {
            userDetailsService.loadUserByUsername("nonexistent@example.com")
        }
    }

} 