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

fun Route.authRoutes() {
    val repository = PostgresUserRepository()

    route("/api/auth") {

        post {
            val obj: LoginDTO = call.receive<LoginDTO>();

            val login = obj.login?: return@post call.respondText(
                "Missing login",
                status = HttpStatusCode.BadRequest
            )

            val password = obj.password?: return@post call.respondText(
                "Missing password",
                status = HttpStatusCode.BadRequest
            )

            val user = repository.userByUserName(login)?: return@post call.respondText(
                "Missing user",
                status = HttpStatusCode.BadRequest
            )

            if (user.password != password) {
                return@post call.respondText(
                    "Error password",
                    status = HttpStatusCode.BadRequest
                )
            }

            val jwtSecret = "your-secret-key"
            val token = generateJwtToken(user, jwtSecret)

            val response = ResponseRegistrationDto (
                token = token,
                user = user
            );

            call.respond(response)
        }

        post("/registration") {

            val registrationDto: RegistrationDTO = call.receive<RegistrationDTO>()

            var inviter = repository.userByInvitationCode(registrationDto.code) ?: return@post call.respondText(
                "No code user code",
                status = HttpStatusCode.NotFound
            )

            val newUserObject = NewUser(
                username = registrationDto.login,
                password = registrationDto.password,
                role = "familiar",
                inviterUserId = inviter.id,
                invitationCode = Random.nextInt(10000).toString(),
                kpi = 0
            )

            val newUser = repository.addUser(newUserObject)

            inviter.kpi += 10;
            repository.updateUser(inviter.id, inviter)

            val jwtSecret = "your-secret-key"
            val token = generateJwtToken(newUser, jwtSecret)

            val response = ResponseRegistrationDto(
                token = token,
                user = newUser
            );

            call.respond(response)

        }

    }
}