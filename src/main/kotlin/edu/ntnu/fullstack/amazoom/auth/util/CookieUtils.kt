package edu.ntnu.fullstack.amazoom.auth.util

import edu.ntnu.fullstack.amazoom.auth.config.AuthProperties
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class CookieUtils (
    private val authProperties: AuthProperties
) {
    fun createAccessTokenCookie(token: String): Cookie {
        return createCookie(
            name = "accessToken",
            value = token,
            maxAge = (authProperties.accessTokenExpiration / 1000).toInt(),
            path = "/"
        )
    }

    fun createRefreshTokenCookie(token: String): Cookie {
        return createCookie(
            name = "refreshToken",
            value = token,
            maxAge = (authProperties.refreshTokenExpiration / 1000).toInt(),
            path = "/auth/refresh"
        )
    }

    fun deleteCookie(name: String, path: String = "/"): Cookie {
        return createCookie(
            name = name,
            value = "",
            maxAge = 0,
            path = path
        )
    }

    fun getCookieValue(request: HttpServletRequest, name: String): String? {
        return request.cookies?.find { it.name == name }?.value
    }

    private fun createCookie(
        name: String,
        value: String,
        maxAge: Int,
        path: String
    ): Cookie {
        return Cookie(name, value).apply {
            this.maxAge = maxAge
            this.path = path
            this.secure = authProperties.secureCookie
            this.isHttpOnly = true
            this.setAttribute("SameSite", "Lax")
        }
    }
}