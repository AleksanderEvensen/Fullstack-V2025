package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Service implementation of Spring Security's UserDetailsService.
 * Loads user-specific data for authentication.
 */
@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {
    private val logger = LoggerFactory.getLogger(UserDetailsServiceImpl::class.java)

    /**
     * Loads a user by their username (email in our case).
     *
     * @param email The user's email address to look up
     * @return A CustomUserDetails object for the authenticated user
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    override fun loadUserByUsername(email: String): CustomUserDetails {

        val user = userRepository.findByEmail(email).orElseThrow {
            logger.warn("User not found with email: {}", email)
            UsernameNotFoundException("User not found with email: $email")
        }

        val authorities = user.roles.map {
            SimpleGrantedAuthority(it.name.toString())
        }

        return CustomUserDetails(
            username = user.email,
            password = user.password,
            authorities = authorities
        )
    }
}

/**
 * Custom implementation of Spring Security's UserDetails.
 * Contains authentication information for a user.
 */
class CustomUserDetails(
    private val username: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities() = authorities
    override fun getPassword() = password
    override fun getUsername() = username
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}