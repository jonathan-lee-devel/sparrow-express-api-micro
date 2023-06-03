package io.jonathanlee.authservice.dto.auth.response

import io.jonathanlee.authservice.enums.auth.LoginStatus

data class LoginResponseDto(
    val loginStatus: LoginStatus,
    val username: String,
    val jwt: String,
)