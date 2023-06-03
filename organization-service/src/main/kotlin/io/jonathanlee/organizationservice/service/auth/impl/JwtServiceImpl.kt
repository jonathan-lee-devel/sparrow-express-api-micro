package io.jonathanlee.organizationservice.service.auth.impl

import io.jonathanlee.organizationservice.service.auth.JwtService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtServiceImpl : JwtService {

    @Value("\${sparrow.jwt.secret-key}")
    private lateinit var secretKey: String

    override fun extractUsername(jwt: String): String? {
        return extractClaim(
            jwt
        ) { obj: Claims? -> obj!!.subject }
    }

    override fun isJwtValid(jwt: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(jwt)
        return (username == userDetails.username) && !isTokenExpired(jwt)
    }

    private fun isTokenExpired(jwt: String): Boolean {
        return extractExpiration(jwt)?.before(Date()) ?: true
    }

    private fun extractExpiration(jwt: String): Date? {
        return extractClaim(
            jwt
        ) { obj: Claims? -> obj!!.expiration }
    }

    private fun <T> extractClaim(jwt: String?, claimsResolver: Function<Claims?, T>): T {
        val claims = extractAllClaims(jwt!!)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(jwt: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(jwt)
            .body
    }

    private fun getSigningKey(): Key {
        return Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
    }

}
