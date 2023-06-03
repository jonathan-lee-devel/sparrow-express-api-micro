package io.jonathanlee.organizationservice.service.auth

import org.springframework.security.core.userdetails.UserDetails

interface JwtService {

    fun extractUsername(jwt: String): String?

    fun isJwtValid(jwt: String, userDetails: UserDetails): Boolean

}
