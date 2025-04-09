package edu.ntnu.fullstack.amazoom.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "minio")
data class MinIOProperties(
    val endpoint: String,
    val accessKey: String,
    val secretKey: String,
    val bucketName: String,
)