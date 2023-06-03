package io.jonathanlee.organizationservice.service.auth.impl

import io.jonathanlee.organizationservice.auth.repository.ApplicationUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ApplicationUserDetailsService(
    private val applicationUserRepository: ApplicationUserRepository,
): UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw IllegalArgumentException("Application user username cannot be null")
        }
        return applicationUserRepository.getApplicationUserByEmail(username)
            ?: throw UsernameNotFoundException("No application user found for username: $username")
    }

}