package io.jonathanlee.authservice.controller.auth

import io.jonathanlee.authservice.dto.auth.request.LoginRequestDto
import io.jonathanlee.authservice.dto.auth.response.LoginResponseDto
import io.jonathanlee.authservice.enums.auth.LoginStatus
import io.jonathanlee.authservice.service.auth.JwtService
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtService: JwtService,
) {

    @PostMapping(
        value = ["/login"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun login(@Valid @RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        this.authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequestDto.username,
                loginRequestDto.password,
            )
        )

        return ResponseEntity
            .ok(
                LoginResponseDto(
                    LoginStatus.SUCCESS,
                    loginRequestDto.username,
                    this.jwtService.generateJwt(this.userDetailsService.loadUserByUsername(loginRequestDto.username))
                )
            )
    }

}