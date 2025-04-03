import edu.ntnu.fullstack.amazoom.auth.service.JwtService
import edu.ntnu.fullstack.amazoom.auth.service.UserDetailsService
import edu.ntnu.fullstack.amazoom.auth.util.CookieUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val cookieUtils: CookieUtils
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val accessToken = cookieUtils.getCookieValue(request, "accessToken")

            if (!accessToken.isNullOrBlank() && jwtService.validateToken(accessToken)) {
                val username = jwtService.getUsernameFromToken(accessToken)
                val userDetails = userDetailsService.loadUserByUsername(username)

                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (ex: Exception) {
            logger.error("Failed to process authentication", ex)
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}