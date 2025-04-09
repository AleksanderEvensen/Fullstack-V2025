package edu.ntnu.fullstack.amazoom.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Amazoom API")
                    .description("""
                        API documentation for the Amazoom marketplace application.
                        
                        This API provides endpoints for user authentication, product listings, 
                        messaging, bookmarks, and more.
                    """.trimIndent())
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("Amazoom Team")
                            .email("contact@amazoom.com")
                    )
                    .license(
                        License()
                            .name("MIT License")
                            .url("https://opensource.org/licenses/MIT")
                    )
            )
    }
} 