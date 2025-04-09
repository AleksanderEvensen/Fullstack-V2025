package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.auth.config.AuthProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

/**
 * Service for JWT token management including generation, validation, and claim extraction.
 * This service is central to the application's security infrastructure.
 */
@Service
class JwtService(
    private val authProperties: AuthProperties
) {
    private val logger = LoggerFactory.getLogger(JwtService::class.java)

    /**
     * Generates a JWT token for an authenticated user.
     *
     * @param userDetails The authenticated user details containing username and authorities
     * @return A signed JWT token string
     */
    fun generateToken(userDetails: CustomUserDetails): String {
        logger.debug("Generating token for user: {}", userDetails.username)

        val now = Date()
        val expiration = Date(now.time + authProperties.tokenExpiration * 1000)

        val roles = userDetails.authorities.map {
            it.authority.removePrefix("ROLE_")
        }.toSet()

        return Jwts.builder()
            .subject(userDetails.username)
            .issuer(authProperties.jwtIssuer)
            .issuedAt(now)
            .claim("roles", roles)
            .expiration(expiration)
            .signWith(getSigningKey())
            .compact()
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token
     * @return The extracted username or null if extraction fails
     */
    fun extractUsername(token: String): String? {
        try {
            return extractClaim(token) { it.subject }
        } catch (e: Exception) {
            logger.warn("Failed to extract username from token", e)
            return null
        }
    }

    /**
     * Validates a JWT token.
     *
     * @param token The JWT token to validate
     * @return True if the token is valid, false otherwise
     * @throws ExpiredJwtException if the token has expired
     */
    fun isTokenValid(token: String): Boolean {
        try {
            val claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .payload

            // Check if token has expired
            if (claims.expiration.before(Date())) {
                logger.warn("Token has expired")
                return false
            }

            return true
        } catch (e: ExpiredJwtException) {
            logger.warn("Token has expired: {}", e.message)
            return false
        } catch (e: SecurityException) {
            logger.error("Invalid signature: {}", e.message)
            return false
        } catch (e: MalformedJwtException) {
            logger.error("Malformed JWT: {}", e.message)
            return false
        } catch (e: UnsupportedJwtException) {
            logger.error("Unsupported JWT: {}", e.message)
            return false
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
            return false
        } catch (e: JwtException) {
            logger.error("JWT validation error: {}", e.message)
            return false
        } catch (e: Exception) {
            logger.error("Unexpected error validating token", e)
            return false
        }
    }

    /**
     * Extracts a specific claim from the token.
     *
     * @param token The JWT token
     * @param claimsResolver Function to extract a specific claim from Claims
     * @return The extracted claim value or null if extraction fails
     */
    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T? {
        val claims = extractAllClaims(token)
        return claims?.let { claimsResolver(it) }
    }

    /**
     * Extracts all claims from the token.
     *
     * @param token The JWT token
     * @return The Claims object or null if extraction fails
     */
    private fun extractAllClaims(token: String): Claims? {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            // Extract claims from expired token (may still be useful for some operations)
            logger.warn("Token expired but extracting claims anyway")
            return e.claims
        } catch (e: Exception) {
            when (e) {
                is SecurityException -> logger.error("Invalid JWT signature", e)
                is MalformedJwtException -> logger.error("Malformed JWT token", e)
                is UnsupportedJwtException -> logger.error("Unsupported JWT token", e)
                is IllegalArgumentException -> logger.error("JWT token compact of handler are invalid", e)
                else -> logger.error("Unexpected error extracting claims", e)
            }
            return null
        }
    }

    /**
     * Gets the signing key for JWT signature verification.
     *
     * @return The secret key used for signing
     */
    private fun getSigningKey(): SecretKey {
        val keyBytes: ByteArray = authProperties.jwtSecret.toByteArray()
        return Keys.hmacShaKeyFor(keyBytes)
    }
}