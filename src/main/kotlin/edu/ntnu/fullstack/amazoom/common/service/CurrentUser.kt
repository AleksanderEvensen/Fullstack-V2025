package edu.ntnu.fullstack.amazoom.common.service

import edu.ntnu.fullstack.amazoom.auth.service.UserDetailsImpl
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class CurrentUser() {

    fun getCurrentAuthenticatedUser(): Optional<UserDetailsImpl> {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal
        return if (principal is UserDetailsImpl) {
            Optional.of(principal)
        } else {
            Optional.empty()
        }

    }
}