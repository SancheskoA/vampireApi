package com.example.routes
import com.example.dto.ResponseRegistrationDto
import kotlin.random.Random
import com.example.models.*
import com.example.plugins.generateJwtToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.notificationRoutes() {

    route("/api/notification") {

        get {
            val repository = call.application.attributes[NotificationRepositoryKey]

            val userId = call.request.queryParameters["user_id"] ?: return@get call.respondText(
                "Missing user id",
                status = HttpStatusCode.NotFound
            )

            val requests = repository.notificationByOwnerId(userId.toInt())

            for (request in requests) {
                request.isSend = true
                repository.updateNotification(request.id, request)
            }

            call.respond(requests)
        }

    }
}