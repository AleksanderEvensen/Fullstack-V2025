package edu.ntnu.fullstack.amazoom.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.auth")
data class AuthProperties (
    val jwtSecret: String,
    val jwtIssuer: String,
    val tokenExpiration: Long,
)