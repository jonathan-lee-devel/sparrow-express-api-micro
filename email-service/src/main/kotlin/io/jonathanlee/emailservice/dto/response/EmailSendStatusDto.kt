package io.jonathanlee.emailservice.dto.response

import io.jonathanlee.emailservice.enums.status.EmailSendStatus

data class EmailSendStatusDto(
    val id: String,
    val status: EmailSendStatus
)
