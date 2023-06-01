package io.jonathanlee.emailservice.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "email_send_attempts")
data class EmailSendAttempt(
    @field:Id val objectId: ObjectId,
    val id: String,
    val addressFrom: String,
    val addressTo: String,
    val subject: String,
    val body: String,
    var isSuccessful: Boolean,
    val retryAttempts: MutableList<EmailSendAttempt>,
    val createdAt: Instant,
    var updatedAt: Instant
)
