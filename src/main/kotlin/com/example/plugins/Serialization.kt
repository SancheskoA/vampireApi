package com.example.plugins

import com.example.models.PostgresRequestRepository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization(repository: PostgresRequestRepository) {

    routing {
        get("/json/kotlinx-serialization") {
                call.respond(mapOf("hello" to "world"))
            }
    }
}
