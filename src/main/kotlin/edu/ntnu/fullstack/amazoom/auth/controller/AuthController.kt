package edu.ntnu.fullstack.amazoom.auth.controller

import edu.ntnu.fullstack.amazoom.auth.dto.AuthResponse
import edu.ntnu.fullstack.amazoom.auth.dto.LoginRequest
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequest
import edu.ntnu.fullstack.amazoom.auth.service.AuthService
import edu.ntnu.fullstack.amazoom.common.dto.FullUserDto
import edu.ntnu.fullstack.amazoom.common.service.UserService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequest,
    ): ResponseEntity<AuthResponse> {
        val authResponse = authService.register(request)
        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ResponseEntity<AuthResponse> {
        val authResponse = authService.login(request.email, request.password)
        return ResponseEntity.ok(authResponse)
    }

    @GetMapping("/me")
    fun me(): ResponseEntity<FullUserDto>{
        val user = userService.getProfile()
        return ResponseEntity.ok(user)
    }

    @GetMapping("/ping")
    fun ping(): ResponseEntity<String> {
        return ResponseEntity.ok("pong")
    }
}