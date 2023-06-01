package io.jonathanlee.emailservice.enums.status

enum class EmailSendStatus {
    FAILURE,
    MAX_RETRY_ATTEMPTS_REACHED,
    SUCCESS
}