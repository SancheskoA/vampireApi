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

    route("/api/auth") {

        post {
            val repository = call.application.attributes[UserRepositoryKey]

            val obj: LoginDTO = call.receive<LoginDTO>();


            val login = obj.login

            val password = obj.password

            val user = repository.userByUserName(login)?: return@post call.respondText(
                "Missing user",
                status = HttpStatusCode.NotFound
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
            val repository = call.application.attributes[UserRepositoryKey]

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