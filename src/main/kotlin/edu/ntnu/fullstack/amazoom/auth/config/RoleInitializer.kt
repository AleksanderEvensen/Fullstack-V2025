package edu.ntnu.fullstack.amazoom.auth.config

import edu.ntnu.fullstack.amazoom.common.entity.Role
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import edu.ntnu.fullstack.amazoom.common.repository.RoleRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RoleInitializer {

    @Bean
    fun initRoles(roleRepository: RoleRepository): CommandLineRunner {
        return CommandLineRunner {
            // Create ROLE_USER if it doesn't exist
            if (roleRepository.findByName(RoleName.ROLE_USER) == null) {
                roleRepository.save(Role(name = RoleName.ROLE_USER))
            }

            // Create ROLE_ADMIN if it doesn't exist
            if (roleRepository.findByName(RoleName.ROLE_ADMIN) == null) {
                roleRepository.save(Role(name = RoleName.ROLE_ADMIN))
            }
        }
    }
}