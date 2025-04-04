package edu.ntnu.fullstack.amazoom

import edu.ntnu.fullstack.amazoom.auth.config.AuthProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AuthProperties::class)
class AmazoomApplication

fun main(args: Array<String>) {
	runApplication<AmazoomApplication>(*args)
}
