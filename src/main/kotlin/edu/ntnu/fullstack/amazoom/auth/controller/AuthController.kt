package edu.ntnu.fullstack.amazoom.auth.controller

import edu.ntnu.fullstack.amazoom.auth.dto.AuthResponseDto
import edu.ntnu.fullstack.amazoom.auth.dto.LoginRequestDto
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequestDto
import edu.ntnu.fullstack.amazoom.auth.dto.UpdatePasswordRequestDto
import edu.ntnu.fullstack.amazoom.auth.service.AuthService
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import edu.ntnu.fullstack.amazoom.common.dto.FullUserDto
import edu.ntnu.fullstack.amazoom.common.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(
    name = "Authentication",
    description = "Authentication API with login, registration, and user profile operations"
)
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
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided registration details and returns an authentication token"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "User registered successfully",
            content = [Content(schema = Schema(implementation = AuthResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input or validation error",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "409",
            description = "User already exists with the provided email or phone number",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user with email and password credentials and returns an authentication token"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Authentication successful",
            content = [Content(schema = Schema(implementation = AuthResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Invalid credentials",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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
    @Operation(
        summary = "Get current user profile",
        description = "Retrieves the profile information of the currently authenticated user"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Profile retrieved successfully",
            content = [Content(schema = Schema(implementation = FullUserDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
    @GetMapping("/me")
    fun me(): ResponseEntity<FullUserDto> {
        val user = userService.getProfile()
        return ResponseEntity.ok(user)
    }

    /**
     * Updates the user's password.
     *
     * @param request The password update request
     * @return Response indicating successful update
     */
    @Operation(
        summary = "Update password",
        description = "Updates the password for the currently authenticated user"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Password updated successfully",
            content = [Content(schema = Schema(implementation = AuthResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input or validation error",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Current password is incorrect",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
    @PostMapping("/update-password")
    fun updatePassword(@Valid @RequestBody request: UpdatePasswordRequestDto): ResponseEntity<AuthResponseDto> {
        val authResponse = authService.updatePassword(
            request.currentPassword,
            request.newPassword
        )

        return ResponseEntity.ok(authResponse)
    }

    /**
     * Simple endpoint to check if authentication is working.
     *
     * @return A simple response to confirm the API is accessible
     */
    @Operation(
        summary = "Authentication test endpoint",
        description = "Simple endpoint to test if authentication is working. Only accessible to admin users."
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Authentication successful (admin access)"
        ),
        ApiResponse(
            responseCode = "403",
            description = "Forbidden - requires admin role",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ping")
    fun ping(): ResponseEntity<String> {
        return ResponseEntity.ok("pong")
    }
}