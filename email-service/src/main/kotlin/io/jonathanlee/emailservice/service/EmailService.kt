package io.jonathanlee.emailservice.service

import io.jonathanlee.emailservice.dto.response.EmailSendStatusDto

interface EmailService {

    fun sendEmail(addressTo: String, subject: String, body: String): EmailSendStatusDto

    fun retrySendEmail(emailSendAttemptId: String): EmailSendStatusDto

}
