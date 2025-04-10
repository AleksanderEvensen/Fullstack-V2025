package edu.ntnu.fullstack.amazoom.config

import edu.ntnu.fullstack.amazoom.common.controller.ImageController
import edu.ntnu.fullstack.amazoom.common.service.IMinIOService
import edu.ntnu.fullstack.amazoom.common.service.MinioService
import edu.ntnu.fullstack.amazoom.common.service.MockMinIOService
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@TestConfiguration
class TestConfig {
    
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Primary
    fun minioService(): IMinIOService {
        return MockMinIOService()
    }


} 