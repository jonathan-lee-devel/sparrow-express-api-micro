package io.jonathanlee.emailservice.service.impl

import io.jonathanlee.emailservice.dto.response.EmailSendStatusDto
import io.jonathanlee.emailservice.enums.status.EmailSendStatus
import io.jonathanlee.emailservice.model.EmailSendAttempt
import io.jonathanlee.emailservice.repository.EmailSendAttemptRepository
import io.jonathanlee.emailservice.service.EmailService
import io.jonathanlee.sparrowexpressapikotlin.service.random.RandomService
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class EmailServiceImpl(
    private val randomService: RandomService,
    private val emailSendAttemptRepository: EmailSendAttemptRepository,
    private val javaMailSender: JavaMailSender
) : EmailService {

    private val logger = LoggerFactory.getLogger(EmailServiceImpl::class.java)

    @Value("\${EMAIL_USERNAME}")
    private lateinit var emailFromAddress: String

    override fun sendEmail(addressTo: String, subject: String, body: String): EmailSendStatusDto {
        val simpleMailMessage = createSimpleMailMessage(addressTo, subject, body)
        val emailSendAttempt = createEmailSendAttempt(addressTo, subject, body)

        this.emailSendAttemptRepository.save(emailSendAttempt)
        return EmailSendStatusDto(emailSendAttempt.id, this.sendEmail(simpleMailMessage, emailSendAttempt, null))
    }

    override fun retrySendEmail(emailSendAttemptId: String): EmailSendStatusDto {
        val originalEmailSendAttempt = this.emailSendAttemptRepository.findById(emailSendAttemptId)
            ?: return EmailSendStatusDto(emailSendAttemptId, EmailSendStatus.FAILURE)

        if (originalEmailSendAttempt.retryAttempts.size > MAX_RETRY_ATTEMPTS) {
            this.logger.warn("Max retry attempt reach for e-mail send attempt with ID: $emailSendAttemptId")
            return EmailSendStatusDto(emailSendAttemptId, EmailSendStatus.MAX_RETRY_ATTEMPTS_REACHED)
        }

        if (originalEmailSendAttempt.isSuccessful) {
            this.logger.warn("Attempting to re-send e-mail send attempt already marked as successful, ID: $emailSendAttemptId")
        }

        val simpleMailMessage = createSimpleMailMessage(originalEmailSendAttempt.addressTo, originalEmailSendAttempt.subject, originalEmailSendAttempt.body)
        val newEmailSendAttempt = createEmailSendAttempt(originalEmailSendAttempt.addressTo, originalEmailSendAttempt.subject, originalEmailSendAttempt.body)
        originalEmailSendAttempt.retryAttempts.add(newEmailSendAttempt)
        originalEmailSendAttempt.updatedAt = Instant.now()
        this.emailSendAttemptRepository.save(originalEmailSendAttempt)
        return EmailSendStatusDto(emailSendAttemptId, this.sendEmail(simpleMailMessage, originalEmailSendAttempt, newEmailSendAttempt))
    }

    private fun createSimpleMailMessage(addressTo: String, subject: String, body: String): SimpleMailMessage {
        val simpleMailMessage = SimpleMailMessage()
        simpleMailMessage.from = this.emailFromAddress
        simpleMailMessage.setTo(addressTo)
        simpleMailMessage.subject = subject
        simpleMailMessage.text = body

        return simpleMailMessage
    }

    private fun createEmailSendAttempt(addressTo: String, subject: String, body: String): EmailSendAttempt {
        return EmailSendAttempt(
            ObjectId.get(),
            this.randomService.generateNewId(),
            this.emailFromAddress,
            addressTo,
            subject,
            body,
            false,
            mutableListOf(),
            Instant.now(),
            Instant.now(),
        )
    }

    private fun sendEmail(simpleMailMessage: SimpleMailMessage, emailSendAttempt: EmailSendAttempt, retryAttempt: EmailSendAttempt?): EmailSendStatus {
        return try {
            this.javaMailSender.send(simpleMailMessage)
            this.logger.info("E-mail with subject [${simpleMailMessage.subject}] sent to address: <${simpleMailMessage.to}>")
            emailSendAttempt.isSuccessful = true
            if (retryAttempt != null) {
                retryAttempt.isSuccessful = true
                retryAttempt.updatedAt = Instant.now()
            }
            this.emailSendAttemptRepository.save(emailSendAttempt)
            EmailSendStatus.SUCCESS
        } catch (mailException: MailException) {
            this.logger.error("An error occurred while sending an e-mail: ${mailException.message}")
            emailSendAttempt.updatedAt = Instant.now()
            if (retryAttempt != null) {
                retryAttempt.updatedAt = Instant.now()
            }
            this.emailSendAttemptRepository.save(emailSendAttempt)
            EmailSendStatus.FAILURE
        }
    }

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 10
    }

}
