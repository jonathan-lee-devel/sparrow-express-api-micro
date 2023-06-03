package io.jonathanlee.organizationservice.auth.repository

import io.jonathanlee.authservice.model.auth.ApplicationUser
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ApplicationUserRepository: MongoRepository<ApplicationUser, ObjectId> {

    fun getApplicationUserById(id: String): ApplicationUser?

    fun getApplicationUserByEmail(email: String): ApplicationUser?

}
