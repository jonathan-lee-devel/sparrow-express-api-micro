package io.jonathanlee.organizationservice.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun test(): ResponseEntity<Any> {
        return ResponseEntity.ok("""
            {
            "greeting":"Hello World!"
            }
        """.trimIndent())
    }

}
