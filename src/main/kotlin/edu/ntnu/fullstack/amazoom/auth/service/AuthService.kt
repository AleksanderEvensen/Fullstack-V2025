package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.auth.dto.AuthResponse
import edu.ntnu.fullstack.amazoom.auth.dto.LoginRequest
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequest
import edu.ntnu.fullstack.amazoom.auth.entity.*
import edu.ntnu.fullstack.amazoom.auth.exception.MissingRoleException
import edu.ntnu.fullstack.amazoom.auth.exception.UserAlreadyExistsException
import edu.ntnu.fullstack.amazoom.auth.repository.RefreshTokenRepository
import edu.ntnu.fullstack.amazoom.auth.repository.RoleRepository
import edu.ntnu.fullstack.amazoom.auth.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val roleRepository: RoleRepository
) {
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException("User with email ${request.email} already exists")
        }

        val userRole = roleRepository.findByName(RoleName.ROLE_USER) ?: throw MissingRoleException()

        val user = User(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            phoneNumber = request.phoneNumber,
            address = Address(),
            roles = mutableSetOf(userRole)
        )

        userRepository.save(user)
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

    fun authenticate(request: LoginRequest): AuthResponse{
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
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
}