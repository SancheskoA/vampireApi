package com.example.plugins

import com.example.routes.authRoutes
import com.example.routes.requestRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*


fun Application.configureRouting() {
    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respondText(text = "400: $cause" , status = HttpStatusCode.BadRequest)
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)

        }


    }
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        authRoutes()
        requestRoutes()
    }
}
