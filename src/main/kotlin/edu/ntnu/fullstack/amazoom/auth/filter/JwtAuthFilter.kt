import edu.ntnu.fullstack.amazoom.auth.service.JwtService
import edu.ntnu.fullstack.amazoom.auth.service.UserDetailsServiceImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filter to process JWT authentication for each request
 */
@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtAuthFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.warn("No valid Authorization header found, skipping JWT processing")
                filterChain.doFilter(request, response)
                return
            }

            val token = authHeader.substring(7)

            if (!jwtService.isTokenValid(token)) {
                logger.warn("Invalid JWT token detected, continuing filter chain")
                filterChain.doFilter(request, response)
                return
            }

            val username = jwtService.extractUsername(token)
            if (username == null) {
                logger.warn("Could not extract username from token")
                filterChain.doFilter(request, response)
                return
            }


            if (SecurityContextHolder.getContext().authentication == null) {
                try {
                    // Load user details and set authentication
                    val userDetails = userDetailsService.loadUserByUsername(username)

                    val authentication = UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        userDetails.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                    SecurityContextHolder.getContext().authentication = authentication
                    logger.info("Successfully authenticated user: $username")

                } catch (e: UsernameNotFoundException) {
                    logger.error("User not found: $username", e)
                } catch (e: Exception) {
                    logger.error("Failed to authenticate user: $username", e)
                }
            }
        } catch (e: Exception) {
            logger.error("Unexpected error in JWT authentication filter", e)
        }

        filterChain.doFilter(request, response)
    }
}