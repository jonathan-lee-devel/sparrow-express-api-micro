package io.jonathanlee.registrationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RegistrationServiceApplication

fun main(args: Array<String>) {
	runApplication<RegistrationServiceApplication>(*args)
}
