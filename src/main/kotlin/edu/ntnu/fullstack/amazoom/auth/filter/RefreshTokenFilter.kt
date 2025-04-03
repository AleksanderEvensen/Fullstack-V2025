package edu.ntnu.fullstack.amazoom.auth.filter

import edu.ntnu.fullstack.amazoom.auth.config.AuthProperties
import edu.ntnu.fullstack.amazoom.auth.entity.RefreshToken
import edu.ntnu.fullstack.amazoom.auth.repository.RefreshTokenRepository
import edu.ntnu.fullstack.amazoom.auth.service.JwtService
import edu.ntnu.fullstack.amazoom.auth.service.UserDetailsImpl
import edu.ntnu.fullstack.amazoom.auth.service.UserDetailsService
import edu.ntnu.fullstack.amazoom.auth.util.CookieUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Instant

@Component
class RefreshTokenFilter(
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userDetailsService: UserDetailsService,
    private val cookieUtils: CookieUtils,
    private val authProperties: AuthProperties
) : OncePerRequestFilter() {
    companion object {
        const val REFRESH_TOKEN_PATH = "/auth/refresh"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath == REFRESH_TOKEN_PATH && request.method == "POST") {
            try {
                val refreshToken = cookieUtils.getCookieValue(request, "refreshToken")
                if (!refreshToken.isNullOrBlank() && jwtService.validateToken(refreshToken)) {
                    val dbToken = refreshTokenRepository.findByToken(refreshToken)

                    if (dbToken != null && dbToken.expiresAt.isAfter(Instant.now())) {
                        val username = jwtService.getUsernameFromToken(refreshToken)
                        val userDetails = userDetailsService.loadUserByUsername(username)
                        val domainUser = (userDetails as UserDetailsImpl).getDomainUser()

                        val newAccessToken = jwtService.generateAccessToken(userDetails)
                        val newRefreshToken = jwtService.generateRefreshToken(userDetails)

                        refreshTokenRepository.delete(dbToken)
                        refreshTokenRepository.save(
                            RefreshToken(
                                token = newRefreshToken,
                                user = domainUser,
                                expiresAt = Instant.now()
                                    .plusMillis(authProperties.refreshTokenExpiration)
                            )
                        )

                        response.addCookie(cookieUtils.createAccessTokenCookie(newAccessToken))
                        response.addCookie(cookieUtils.createRefreshTokenCookie(newRefreshToken))
                        return
                    }
                }
            } catch (ex: Exception) {
                logger.error("Refresh token processing failed", ex)
            }

            response.addCookie(cookieUtils.deleteCookie("accessToken"))
            response.addCookie(cookieUtils.deleteCookie("refreshToken"))
            response.sendError(HttpStatus.UNAUTHORIZED.value())
            return
        }
        filterChain.doFilter(request, response)
    }
}