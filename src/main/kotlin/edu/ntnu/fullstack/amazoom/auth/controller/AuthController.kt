package edu.ntnu.fullstack.amazoom.auth.controller

import edu.ntnu.fullstack.amazoom.auth.dto.AuthResponse
import edu.ntnu.fullstack.amazoom.auth.dto.LoginRequest
import edu.ntnu.fullstack.amazoom.auth.dto.RegisterRequest
import edu.ntnu.fullstack.amazoom.auth.service.AuthService
import edu.ntnu.fullstack.amazoom.auth.util.CookieUtils
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieUtils: CookieUtils
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequest,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val authResponse = authService.register(request)
        setAuthCookies(response, authResponse)
        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<AuthResponse> {
        val authResponse = authService.authenticate(request)
        setAuthCookies(response, authResponse)
        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/logout")
    fun logout(
        response: HttpServletResponse
    ): ResponseEntity<Void> {
        response.addCookie(cookieUtils.deleteCookie("accessToken"))
        response.addCookie(cookieUtils.deleteCookie("refreshToken", "/auth/refresh"))
        return ResponseEntity.noContent().build()
    }

    private fun setAuthCookies(response: HttpServletResponse, authResponse: AuthResponse) {
        response.addCookie(cookieUtils.createAccessTokenCookie(authResponse.accessToken))
        response.addCookie(cookieUtils.createRefreshTokenCookie(authResponse.refreshToken))
    }
}