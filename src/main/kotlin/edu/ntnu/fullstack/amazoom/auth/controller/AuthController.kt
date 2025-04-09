package edu.ntnu.fullstack.amazoom.auth.controller

import edu.ntnu.fullstack.amazoom.auth.dto.AuthResponseDto
import edu.ntnu.fullstack.amazoom.auth.dto.LoginRequestDto
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequestDto
import edu.ntnu.fullstack.amazoom.auth.service.AuthService
import edu.ntnu.fullstack.amazoom.common.dto.FullUserDto
import edu.ntnu.fullstack.amazoom.common.service.UserService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for authentication operations.
 * Handles user registration, login, and profile information.
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    /**
     * Registers a new user in the system.
     *
     * @param request The registration request data
     * @return Authentication response with access token
     */
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequestDto,
    ): ResponseEntity<AuthResponseDto> {
        val authResponse = authService.register(request)
        return ResponseEntity.ok(authResponse)
    }

    /**
     * Authenticates a user with their credentials.
     *
     * @param request The login request containing credentials
     * @return Authentication response with access token
     */
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequestDto,
    ): ResponseEntity<AuthResponseDto> {
        val authResponse = authService.login(request.email, request.password)
        return ResponseEntity.ok(authResponse)
    }

    /**
     * Retrieves the profile information of the authenticated user.
     *
     * @return The user's profile information
     */
    @GetMapping("/me")
    fun me(): ResponseEntity<FullUserDto>{
        val user = userService.getProfile()
        return ResponseEntity.ok(user)
    }

    /**
     * Simple endpoint to check if authentication is working.
     *
     * @return A simple response to confirm the API is accessible
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ping")
    fun ping(): ResponseEntity<String> {
        return ResponseEntity.ok("pong")
    }
}