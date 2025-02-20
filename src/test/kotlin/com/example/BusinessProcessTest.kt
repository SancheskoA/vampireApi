package com.example

import com.example.plugins.*
import com.example.models.*

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*

class BusinessProcessTest {

    @Test
    fun testLoginUC() = testApplication {
        // Мокируем репозиторий
        val mockRepository = object : UserRepository {
            override suspend fun allUsers(): List<User> = emptyList()
            override suspend fun userById(id: Int): User? = null
            override suspend fun userByUserName(username: String): User? = User(
                id = 1,
                username = "testUser",
                password = "testPass",
                role = "user",
                inviterUserId = null,
                invitationCode = "1234",
                kpi = 0
            )
            override suspend fun userByInvitationCode(invitationCode: String): User? = null
            override suspend fun addUser(user: NewUser): User = User(
                id = 2,
                username = user.username,
                password = user.password,
                role = user.role,
                inviterUserId = user.inviterUserId,
                invitationCode = user.invitationCode,
                kpi = 0
            )
            override suspend fun updateUser(id: Int, user: User) {}
            override suspend fun removeUser(id: Int) {}
        }

        application {

            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(UserRepositoryKey, mockRepository)
            }
        }

        val loginDTO = LoginDTO(login = "testUser", password = "testPass")
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/api/auth") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(loginDTO)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("token"))
    }

    @Test
    fun testRegistrationSuccessAndKpiIncrementUS() = testApplication {
        // Мокируем репозиторий
        val mockRepository = object : UserRepository {
            override suspend fun allUsers(): List<User> = emptyList()
            override suspend fun userById(id: Int): User? = null
            override suspend fun userByUserName(username: String): User? = null
            override suspend fun userByInvitationCode(invitationCode: String): User? = User(
                id = 1,
                username = "inviterUser",
                password = "inviterPass",
                role = "admin",
                inviterUserId = null,
                invitationCode = "1234",
                kpi = 0 // Начальное значение KPI
            )
            override suspend fun addUser(user: NewUser): User = User(
                id = 2,
                username = user.username,
                password = user.password,
                role = user.role,
                inviterUserId = user.inviterUserId,
                invitationCode = user.invitationCode,
                kpi = 0
            )
            override suspend fun updateUser(id: Int, user: User) {
                // Проверяем, что KPI обновляется корректно
                if (id == 1) {
                    assertEquals(10, user.kpi) // Ожидаем, что KPI увеличится на 10
                }
            }
            override suspend fun removeUser(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(UserRepositoryKey, mockRepository)
            }
        }

        // Создаем объект RegistrationDTO
        val registrationDTO = RegistrationDTO(
            login = "newUser",
            password = "newPass",
            code = "1234" // Код приглашения
        )

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // Отправляем запрос на регистрацию
        val response = client.post("/api/auth/registration") {
            contentType(ContentType.Application.Json)
            setBody(registrationDTO)
        }

        // Проверяем ответ
        assertEquals(HttpStatusCode.OK, response.status)

        // Проверяем, что ответ содержит токен и данные пользователя
        val responseBody = response.bodyAsText()
        assertTrue(responseBody.contains("token"))
        assertTrue(responseBody.contains("newUser"))

        // Проверяем, что KPI пригласившего пользователя увеличился на 10
        // Это проверяется внутри мок-репозитория в методе updateUser
    }

    @Test
    fun testDoneRequestUC() = testApplication {
        val mockRequestRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = Request(
                id = 1,
                title = "Done Request",
                status = STATUS.DONE.description,
                executorUserId = 2,
                type = "people"
            )
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                people = "New Request",
                comment = "New Request",
                creatorUserId = 1,
                executorUserId = 1,
                mapPoint = "Dom"
            )
            override suspend fun updateRequest(id: Int, request: Request) {
                assertEquals(STATUS.DONE.description, request.status)
            }
            override suspend fun removeRequest(id: Int) {}
        }

        val mockUserRepository = object : UserRepository {
            override suspend fun allUsers(): List<User> = emptyList()
            override suspend fun userById(id: Int): User? = User(
                id = 2,
                username = "executor",
                password = "password",
                role = "user",
                kpi = 0,
                invitationCode = "123",
                inviterUserId = 1
            )
            override suspend fun userByUserName(username: String): User? = null
            override suspend fun userByInvitationCode(invitationCode: String): User? = null
            override suspend fun addUser(user: NewUser): User = User(
                id = 2,
                username = user.username,
                password = user.password,
                role = user.role,
                kpi = 0,
                invitationCode = "123",
                inviterUserId = 1
            )
            override suspend fun updateUser(id: Int, user: User) {
                assertEquals(500, user.kpi) // Проверяем, что KPI увеличился на 500
            }
            override suspend fun removeUser(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRequestRepository)
                it.attributes.put(UserRepositoryKey, mockUserRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.put("/api/requests/1/done")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Done Request"))
    }

    @Test
    fun testGetVampireRequestsUC() = testApplication {
        // Мокируем репозиторий
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = listOf(
                Request(
                    id = 1,
                    title = "Vampire Request",
                    type = "vampire",
                    status = STATUS.NEW.description,
                    people = "New Request",
                    comment = "New Request",
                    creatorUserId = 1,
                    executorUserId = 1,
                    mapPoint = "Dom"
                )
            )
            override suspend fun requestById(id: Int): Request? = null
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                people = "New Request",
                comment = "New Request",
                creatorUserId = 1,
                executorUserId = 1,
                mapPoint = "Dom"
            )
            override suspend fun updateRequest(id: Int, request: Request) {}
            override suspend fun removeRequest(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/api/requests/vampire")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Vampire Request"))
    }

}