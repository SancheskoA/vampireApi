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

class AuthRoutesTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun testLoginSuccess() = testApplication {
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
    fun testLoginPassError() = testApplication {
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

        val loginDTO = LoginDTO(login = "testUser", password = "testError")
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/api/auth") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(loginDTO)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun testLoginUserNameMiss() = testApplication {
        // Мокируем репозиторий
        val mockRepository = object : UserRepository {
            override suspend fun allUsers(): List<User> = emptyList()
            override suspend fun userById(id: Int): User? = null
            override suspend fun userByUserName(username: String): User? = null
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

        val loginDTO = LoginDTO(login = "userName", password = "testError")
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/api/auth") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(loginDTO)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Missing user", response.bodyAsText())
    }

    @Test
    fun testRegistrationSuccess() = testApplication {
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
                kpi = 0
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
    }

    @Test
    fun testRegistrationFailureInvalidCode() = testApplication {
        // Мокируем репозиторий
        val mockRepository = object : UserRepository {
            override suspend fun allUsers(): List<User> = emptyList()
            override suspend fun userById(id: Int): User? = null
            override suspend fun userByUserName(username: String): User? = null
            override suspend fun userByInvitationCode(invitationCode: String): User? = null // Код не найден
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

        // Создаем объект RegistrationDTO с неверным кодом
        val registrationDTO = RegistrationDTO(
            login = "newUser",
            password = "newPass",
            code = "wrongCode" // Неверный код приглашения
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

        // Проверяем, что сервер вернул ошибку
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("No code user code", response.bodyAsText())
    }

    @Test
    fun testRegistrationSuccessAndKpiIncrement() = testApplication {
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


}
