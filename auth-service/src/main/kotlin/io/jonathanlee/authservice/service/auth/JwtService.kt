package io.jonathanlee.authservice.service.auth

import org.springframework.security.core.userdetails.UserDetails

interface JwtService {

    fun extractUsername(jwt: String): String?

    fun generateJwt(userDetails: UserDetails): String

    fun isJwtValid(jwt: String, userDetails: UserDetails): Boolean

}
