package edu.ntnu.fullstack.amazoom.chat.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity

@Configuration
@EnableWebSocketSecurity
class WebSocketSecurityConfig {
    @Bean
    fun messageSecurityMetadataSourceRegistry(): MessageSecurityMetadataSourceRegistry {
        return MessageSecurityMetadataSourceRegistry()
            .simpDestMatchers("/app/**").authenticated()
            .simpSubscribeDestMatchers("/user/queue/**").authenticated()
            .anyMessage().denyAll()
    }
}