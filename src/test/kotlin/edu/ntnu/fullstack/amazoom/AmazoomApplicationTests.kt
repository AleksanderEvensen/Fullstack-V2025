package edu.ntnu.fullstack.amazoom

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@EntityScan(basePackages = ["edu.ntnu.fullstack.amazoom"])
class AmazoomApplicationTests {
    
    @Test
    fun contextLoads() {
        // Verify that the Spring context loads correctly
    }
}
