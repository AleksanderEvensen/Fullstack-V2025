package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.auth.dto.AuthResponse
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequest
import edu.ntnu.fullstack.amazoom.common.dto.CreateUserDto
import edu.ntnu.fullstack.amazoom.common.exception.UserNotFoundException
import edu.ntnu.fullstack.amazoom.common.service.UserService
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: UserDetailsServiceImpl,
) {
    @Transactional
    fun register(data: RegisterRequest): AuthResponse {
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

        return AuthResponse(
            accessToken = accessToken,
        )
    }

    @Transactional
    fun login(email: String, password: String): AuthResponse {
        val user = userService.getUserByEmail(email)

        val passwordMatches = passwordEncoder.matches(password, user.password)

        if (!passwordMatches) {
            throw UserNotFoundException()
        }

        val userDetails = userDetailsService.loadUserByUsername(email)
        val accessToken = jwtService.generateToken(userDetails)

        return AuthResponse(
            accessToken = accessToken,
        )
    }
}