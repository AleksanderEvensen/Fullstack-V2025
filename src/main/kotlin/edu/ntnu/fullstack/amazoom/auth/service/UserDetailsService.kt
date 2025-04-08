package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.common.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): CustomUserDetails {
        val user = userRepository.findByEmail(email).orElseThrow {
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