package io.jonathanlee.emailservice.service.random.impl

import io.jonathanlee.constraint.auth.CommonConstraints
import io.jonathanlee.sparrowexpressapikotlin.service.random.RandomService
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.Base64

@Service
class RandomServiceImpl(
    private val secureRandom: SecureRandom = SecureRandom()
) : RandomService {

    override fun generateNewId(): String {
        return this.generateString(CommonConstraints.ID_LENGTH)
    }

    override fun generateNewTokenValue(): String {
        return this.generateString(CommonConstraints.TOKEN_LENGTH)
    }

    private fun generateString(length: Int): String {
        require(length >= 1 && length <= CommonConstraints.TOKEN_LENGTH) { "Randomly generated strings must be between length 1 and ${CommonConstraints.TOKEN_LENGTH}" }
        val bytes = ByteArray(length)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, length)
    }

}