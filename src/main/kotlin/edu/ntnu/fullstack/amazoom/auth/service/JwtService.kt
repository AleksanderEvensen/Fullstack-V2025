package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.auth.config.AuthProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MissingClaimException
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    private val authProperties: AuthProperties
) {
    private fun getSignedKey(): SecretKey {
        val keyBytes = authProperties.jwtSecret.toByteArray()
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateAccessToken(userDetails: UserDetails): String {
        val now = Date()
        val expiration = Date(now.time + authProperties.accessTokenExpiration)
        val roles = userDetails.authorities.map {
            it.authority.removePrefix("ROLE_")
        }.toSet()

        return Jwts.builder()
            .subject(userDetails.username)
            .issuer(authProperties.jwtIssuer)
            .issuedAt(now)
            .claim("roles", roles)
            .expiration(expiration)
            .signWith(getSignedKey())
            .compact()
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        val now = Date()
        val expiration = Date(now.time + authProperties.refreshTokenExpiration)

        return Jwts.builder()
            .subject(userDetails.username)
            .issuer(authProperties.jwtIssuer)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(getSignedKey())
            .compact()
    }

    fun getEmailFromToken(token: String): String {
        val claims = getAllClaimsFromToken(token)
        return claims.subject
    }

    fun getExpirationDateFromToken(token: String): Date {
        val claims = getAllClaimsFromToken(token)
        return claims.expiration
    }

    fun validateToken(token: String): Boolean {
        return try {
            val parser = Jwts.parser()
                .verifyWith(getSignedKey())
                .requireIssuer(authProperties.jwtIssuer)
                .build()

            val jwt = parser.parseSignedClaims(token)

            validateTokenStructure(jwt)

            true
        } catch (e: Exception) {
            false
        }
    }

    private fun validateTokenStructure(claims: Jwt<*, Claims>) {
        val payload = claims.payload
        val header = claims.header

        // Validate claims
        if (payload.subject.isNullOrEmpty()) {
            throw MissingClaimException(header, payload, "sub", payload.subject, "Missing subject")
        }
        if (payload.issuer != authProperties.jwtIssuer) {
            throw MissingClaimException(header, payload, "iss", payload.issuer, "Invalid issuer")
        }

        // Validate expiration
        if (payload.expiration.before(Date())) {
            throw ExpiredJwtException(header, payload, "Token expired")
        }
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSignedKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }
}