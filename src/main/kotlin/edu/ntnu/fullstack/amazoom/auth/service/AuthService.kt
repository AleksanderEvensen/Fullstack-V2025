package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.auth.config.AuthProperties
import edu.ntnu.fullstack.amazoom.auth.dto.AuthResponse
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequest
import edu.ntnu.fullstack.amazoom.auth.entity.*
import edu.ntnu.fullstack.amazoom.auth.exception.*
import edu.ntnu.fullstack.amazoom.auth.repository.RefreshTokenRepository
import edu.ntnu.fullstack.amazoom.common.dto.CreateUserDto
import edu.ntnu.fullstack.amazoom.common.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AuthService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val authProperties: AuthProperties,
    private val userService: UserService
) {
    fun register(data: RegisterRequest): AuthResponse {
        val user = userService.createUser(CreateUserDto(
            firstName = data.firstName,
            lastName = data.lastName,
            email = data.email,
            phoneNumber = data.phoneNumber,
            password = data.password
        ))

        val userDetails = UserDetailsImpl(user)

        val accessToken = jwtService.generateAccessToken(userDetails)
        val refreshToken = jwtService.generateRefreshToken(userDetails)

        refreshTokenRepository.save(
            RefreshToken(
                token = refreshToken,
                user = user,
                expiresAt = jwtService.getExpirationDateFromToken(refreshToken).toInstant()
            )
        )

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    fun authenticate(email: String, password: String): AuthResponse{
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(email, password)
        )

        val userDetails = authentication.principal as UserDetailsImpl

        val accessToken = jwtService.generateAccessToken(userDetails)
        val refreshToken = jwtService.generateRefreshToken(userDetails)

        refreshTokenRepository.save(
            RefreshToken(
                token = refreshToken,
                user = userDetails.getDomainUser(),
                expiresAt = jwtService.getExpirationDateFromToken(refreshToken).toInstant()
            )
        )

        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    fun logout(
        refreshToken: String
    ) {
        refreshTokenRepository.findByToken(refreshToken)?.let { token ->
            refreshTokenRepository.delete(token)
        }
    }

    fun refreshToken(
        refreshToken: String,
    ): AuthResponse {
        // Check if the refresh token is present
        if(!jwtService.validateToken(refreshToken)){
            throw InvalidTokenException("Invalid refresh token")
        }

        // Check if the token exists in the database
        val dbToken = refreshTokenRepository.findByToken(refreshToken)
            ?: throw MissingTokenException("Refresh token not found")

        // Check if the token is expired
        if(dbToken.expiresAt.isBefore(Instant.now())){
            refreshTokenRepository.delete(dbToken)
            throw TokenExpiredException("Refresh token has expired")
        }

        // Get user details
        val email = jwtService.getEmailFromToken(refreshToken)
        val userDetails = userDetailsService.loadUserByUsername(email) as UserDetailsImpl

        // Generate new tokens
        val newAccessToken = jwtService.generateAccessToken(userDetails)
        val newRefreshToken = jwtService.generateRefreshToken(userDetails)

        // Update database
        refreshTokenRepository.delete(dbToken)
        refreshTokenRepository.save(
            RefreshToken(
                token = newRefreshToken,
                user = userDetails.getDomainUser(),
                expiresAt = Instant.now().plusMillis(authProperties.refreshTokenExpiration)
            )
        )

        return AuthResponse(newAccessToken, newRefreshToken)
    }
}