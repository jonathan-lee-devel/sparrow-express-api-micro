package io.jonathanlee.emailservice.controller

import io.jonathanlee.emailservice.dto.response.EmailSendStatusDto
import io.jonathanlee.emailservice.enums.status.EmailSendStatus
import io.jonathanlee.emailservice.service.EmailService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mail")
class EmailController(
    private val emailService: EmailService
) {

    @PostMapping(
        value = ["/send"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun sendEmail(): ResponseEntity<EmailSendStatusDto> {
        val emailSendStatusDto = this.emailService.sendEmail("jonathan.lee.devel@gmail.com", "Test", "This is a test!")
        return when(emailSendStatusDto.status) {
            EmailSendStatus.SUCCESS -> ResponseEntity.ok(emailSendStatusDto)
            EmailSendStatus.FAILURE -> ResponseEntity.internalServerError().body(emailSendStatusDto)
        }
    }

    @PatchMapping(
        value = ["/retry/{sendAttemptId}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun retryEmail(@PathVariable sendAttemptId: String): ResponseEntity<EmailSendStatusDto> {
        val emailSendStatusDto = this.emailService.retrySendEmail(sendAttemptId)
        return when(emailSendStatusDto.status) {
            EmailSendStatus.SUCCESS -> ResponseEntity.ok(emailSendStatusDto)
            EmailSendStatus.FAILURE -> ResponseEntity.internalServerError().body(emailSendStatusDto)
        }
    }

}
