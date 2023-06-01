package io.jonathanlee.passwordservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PasswordServiceApplication

fun main(args: Array<String>) {
	runApplication<PasswordServiceApplication>(*args)
}
