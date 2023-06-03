package io.jonathanlee.organizationservice.filter.auth

import io.jonathanlee.organizationservice.service.auth.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
): OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI.startsWith("/organizations/public")) {
            log.info("Request with URI: ${request.requestURI} bypasses JWT authentication filter")
            filterChain.doFilter(request, response)
            return
        }
        val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER_KEY)
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.status = HttpStatus.UNAUTHORIZED.ordinal
            filterChain.doFilter(request, response)
            return
        }
        val jwt = authorizationHeader.substring(BEARER_START_INDEX)
        val username = this.jwtService.extractUsername(jwt)
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = this.userDetailsService.loadUserByUsername(username)
            if (this.jwtService.isJwtValid(jwt, userDetails)) {
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
                log.info("Successful JWT authentication filtering at URI: ${request.requestURI} for user with e-mail <$username>")
            }
            filterChain.doFilter(request, response)
        }
    }

    companion object {
        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
        private const val BEARER_START_INDEX = 7
    }

}