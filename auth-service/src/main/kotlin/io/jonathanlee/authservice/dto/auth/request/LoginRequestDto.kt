package io.jonathanlee.authservice.dto.auth.request

import jakarta.validation.constraints.NotNull

data class LoginRequestDto(
    @field:NotNull val username: String,
    @field:NotNull val password: String
)
