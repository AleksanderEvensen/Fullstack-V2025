package edu.ntnu.fullstack.amazoom

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AmazoomApplication

fun main(args: Array<String>) {
	runApplication<AmazoomApplication>(*args)
}
