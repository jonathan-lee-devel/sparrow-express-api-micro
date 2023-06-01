package io.jonathanlee.emailservice.repository

import io.jonathanlee.emailservice.model.EmailSendAttempt
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface EmailSendAttemptRepository: MongoRepository<EmailSendAttempt, ObjectId> {

    fun findById(id: String): EmailSendAttempt?

}