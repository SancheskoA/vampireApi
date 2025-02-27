package com.example.routes

import com.example.dto.ResponseRequestDto
import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.plugins.STATUS

fun Route.requestRoutes() {

    route("/api/requests") {
        get("/vampire") {
            val repository = call.application.attributes[RequestRepositoryKey]

            val requests = repository.allRequestFilter(null, null)
            call.respond(requests)
        }

        get("/history") {
            val repository = call.application.attributes[RequestRepositoryKey]

            val userId = call.request.queryParameters["user_id"] ?: return@get call.respondText(
                "Missing user id",
                status = HttpStatusCode.NotFound
            )

            val requests = repository.allRequestFilter("history", userId.toInt())
            call.respond(requests)
        }

        get("/meets") {
            val repository = call.application.attributes[RequestRepositoryKey]

            val userId = call.request.queryParameters["user_id"] ?: return@get call.respondText(
                "Missing user id",
                status = HttpStatusCode.NotFound
            )

            val requests = repository.allRequestFilter("meets", userId.toInt())
            call.respond(requests)
        }

        get("/active") {
            val repository = call.application.attributes[RequestRepositoryKey]

            val userId = call.request.queryParameters["user_id"] ?: return@get call.respondText(
                "Missing user id",
                status = HttpStatusCode.NotFound
            )

            val type = call.request.queryParameters["type"]

            val request = repository.getActiveRequest(userId.toInt(), type) ?: return@get call.respond(
                HttpStatusCode.OK
            )

            call.respond(request)
        }

        post {
            val repository = call.application.attributes[RequestRepositoryKey]

            val userId = call.request.queryParameters["user_id"] ?: return@post call.respondText(
                "Missing user id",
                status = HttpStatusCode.NotFound
            )

            val newRequestDto: NewRequest = call.receive<NewRequest>()
            newRequestDto.status = "Новый"
            newRequestDto.creatorUserId = userId.toInt()

            val newRequest = repository.addRequest(newRequestDto)

            val response = ResponseRequestDto(
                order = newRequest
            )

            call.respond(response)
        }

        delete("/{id}/cancel") {
            val repository = call.application.attributes[RequestRepositoryKey]
            val notificationRepository = call.application.attributes[NotificationRepositoryKey]

            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val request = repository.requestById(id.toInt()) ?: return@delete call.respondText(
                "No request with id $id",
                status = HttpStatusCode.NotFound
            )

            if (request.executorUserId != null) {
                val notification = NewNotification(
                    title = "Уведомление о заказе",
                    body = "Заказ № " + request.id + " " + STATUS.CANCELED.description,
                    ownerId = request.executorUserId
                )
                notificationRepository.addNotification(notification)
            }
            request.status = STATUS.CANCELED.description
            request.executorUserId = null
            repository.updateRequest(request.id, request)

            call.respond(request)
        }

        put("/{id}/done") {
            val repository = call.application.attributes[RequestRepositoryKey]
            val userRepository = call.application.attributes[UserRepositoryKey]
            val notificationRepository = call.application.attributes[NotificationRepositoryKey]

            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val request = repository.requestById(id.toInt()) ?: return@put call.respondText(
                "No request with id $id",
                status = HttpStatusCode.NotFound
            )

            request.status = STATUS.DONE.description
            repository.updateRequest(request.id, request)

            val executorUserId = request.executorUserId ?: return@put call.respondText(
                "No executorUserId",
                status = HttpStatusCode.NotFound
            )

            val user = userRepository.userById(executorUserId) ?: return@put call.respondText(
                "No user with executorUserId",
                status = HttpStatusCode.NotFound
            )

            val notification = NewNotification(
                title = "Уведомление о заказе",
                body = "Заказ № " + request.id + " " + STATUS.DONE.description,
                ownerId = request.executorUserId
            )
            notificationRepository.addNotification(notification)

            user.kpi += if (request.type === "people") 500 else 200
            userRepository.updateUser(user.id, user)

            call.respond(request)
        }

        put("/{id}/take") {
            val repository = call.application.attributes[RequestRepositoryKey]
            val notificationRepository = call.application.attributes[NotificationRepositoryKey]

            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val request = repository.requestById(id.toInt()) ?: return@put call.respondText(
                "No request with id $id",
                status = HttpStatusCode.NotFound
            )

            val userId = call.request.queryParameters["user_id"] ?: return@put call.respondText(
                "Missing user id",
                status = HttpStatusCode.NotFound
            )

            val notification = NewNotification(
                title = "Уведомление о заказе",
                body = "Заказ № " + request.id + " " + STATUS.SEARCH_FOR_RESIDENT.description,
                ownerId = request.creatorUserId
            )
            notificationRepository.addNotification(notification)

            request.status = STATUS.SEARCH_FOR_RESIDENT.description
            request.executorUserId = userId.toInt()
            repository.updateRequest(request.id, request)

            call.respond(request)
        }

        put("/{id}/find") {
            val repository = call.application.attributes[RequestRepositoryKey]
            val notificationRepository = call.application.attributes[NotificationRepositoryKey]

            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val request = repository.requestById(id.toInt()) ?: return@put call.respondText(
                "No request with id $id",
                status = HttpStatusCode.NotFound
            )

            val notification = NewNotification(
                title = "Уведомление о заказе",
                body = "Заказ № " + request.id + " " + STATUS.RESIDENT_FOUND.description,
                ownerId = request.creatorUserId
            )
            notificationRepository.addNotification(notification)

            val obj: findRequestDTO = call.receive<findRequestDTO>()

            request.status = STATUS.RESIDENT_FOUND.description
            request.people = obj.people
            repository.updateRequest(request.id, request)

            call.respond(request)
        }

        put("/{id}/review") {
            val repository = call.application.attributes[RequestRepositoryKey]

            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val request = repository.requestById(id.toInt()) ?: return@put call.respondText(
                "No request with id $id",
                status = HttpStatusCode.NotFound
            )

            val obj: reviewRequestDTO = call.receive<reviewRequestDTO>()

            request.review = obj.review
            repository.updateRequest(request.id, request)

            call.respond(request)
        }

        put("/{id}/accept") {
            val repository = call.application.attributes[RequestRepositoryKey]
            val notificationRepository = call.application.attributes[NotificationRepositoryKey]

            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val request = repository.requestById(id.toInt()) ?: return@put call.respondText(
                "No request with id $id",
                status = HttpStatusCode.NotFound
            )

            val userId = call.request.queryParameters["user_id"] ?: return@put call.respondText(
                "Missing user id",
                status = HttpStatusCode.NotFound
            )

            val notification = NewNotification(
                title = "Уведомление о заказе",
                body = "Заказ № " + request.id + " " + STATUS.ACCEPTED.description,
                ownerId = request.creatorUserId
            )
            notificationRepository.addNotification(notification)

            request.status = STATUS.ACCEPTED.description
            request.executorUserId = userId.toInt()
            repository.updateRequest(request.id, request)

            call.respond(request)
        }

        put("/{id}/courier") {
            val repository = call.application.attributes[RequestRepositoryKey]
            val notificationRepository = call.application.attributes[NotificationRepositoryKey]

            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val request = repository.requestById(id.toInt()) ?: return@put call.respondText(
                "No request with id $id",
                status = HttpStatusCode.NotFound
            )

            val notification = NewNotification(
                title = "Уведомление о заказе",
                body = "Заказ № " + request.id + " " + STATUS.СOURIER.description,
                ownerId = request.creatorUserId
            )
            notificationRepository.addNotification(notification)

            val obj: findRequestDTO = call.receive<findRequestDTO>()

            request.status = STATUS.СOURIER.description
            request.people = obj.people
            repository.updateRequest(request.id, request)

            call.respond(request)
        }

        put("/{id}/refuse") {
            val repository = call.application.attributes[RequestRepositoryKey]
            val notificationRepository = call.application.attributes[NotificationRepositoryKey]

            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val request = repository.requestById(id.toInt()) ?: return@put call.respondText(
                "No request with id $id",
                status = HttpStatusCode.NotFound
            )

            val notification = NewNotification(
                title = "Уведомление о заказе",
                body = "Заказ № " + request.id + " " + STATUS.CANCELED.description,
                ownerId = request.creatorUserId
            )
            notificationRepository.addNotification(notification)

            request.status = STATUS.NEW.description
            request.executorUserId = null
            repository.updateRequest(request.id, request)

            call.respond(request)
        }

        put("/{id}/createVampire") {
            val repository = call.application.attributes[RequestRepositoryKey]
            val userRepository = call.application.attributes[UserRepositoryKey]

            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val request = repository.requestById(id.toInt()) ?: return@put call.respondText(
                "No request with id $id",
                status = HttpStatusCode.NotFound
            )

            request.status = STATUS.DONE.description
            repository.updateRequest(request.id, request)

            val creatorUserId = request.creatorUserId ?: return@put call.respondText(
                "No executorUserId",
                status = HttpStatusCode.NotFound
            )

            val user = userRepository.userById(creatorUserId) ?: return@put call.respondText(
                "No user with executorUserId",
                status = HttpStatusCode.NotFound
            )

            user.role = "supremeVampire"
            userRepository.updateUser(user.id, user)

            call.respond(request)
        }
    }
}