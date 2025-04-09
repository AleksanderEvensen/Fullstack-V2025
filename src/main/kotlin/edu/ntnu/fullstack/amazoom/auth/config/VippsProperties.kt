package edu.ntnu.fullstack.amazoom.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.vipps")
data class VippsProperties (
    val baseUrl: String,
    val clientId: String,
    val clientSecret: String,
    val ocpApimSubkey: String,
    val ocpApimSubkeySecondary: String,
    val merchantSerialNumber: String,
)