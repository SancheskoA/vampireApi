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

class RequestRoutesTest {

    @Test
    fun testGetVampireRequests() = testApplication {
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

    @Test
    fun testGetHistoryRequests() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = listOf(
                Request(
                    id = 1,
                    title = "History Request",
                    type = "history",
                    status = STATUS.DONE.description,
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

        val response = client.get("/api/requests/history?user_id=1")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("History Request"))
    }

    @Test
    fun testGetMeetsRequests() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = listOf(
                Request(
                    id = 1,
                    title = "Meet Request",
                    type = "meet",
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

        val response = client.get("/api/requests/meets?user_id=1")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Meet Request"))
    }

    @Test
    fun testGetActiveRequest() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = Request(
                id = 1,
                title = "Active Request",
                status = STATUS.NEW.description,
                people = "New Request",
                comment = "New Request",
                creatorUserId = 1,
                executorUserId = 1,
                mapPoint = "Dom"
            )
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = Request(
                id = 1,
                title = "Active Request",
                status = STATUS.NEW.description,
                people = "New Request",
                comment = "New Request",
                creatorUserId = 1,
                executorUserId = 1,
                mapPoint = "Dom"
            )
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

        val response = client.get("/api/requests/active?user_id=1")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Active Request"))
    }

    @Test
    fun testCreateRequest() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
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

        val newRequest = NewRequest(
            comment = "Test Comment",
            type = "vampire"
        )

        val response = client.post("/api/requests?user_id=1") {
            contentType(ContentType.Application.Json)
            setBody(newRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("New Request"))
    }

    @Test
    fun testCancelRequest() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = Request(
                id = 1,
                title = "Canceled Request",
                status = STATUS.CANCELED.description,
                people = "New Request",
                comment = "New Request",
                creatorUserId = 1,
                executorUserId = 1,
                mapPoint = "Dom"
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
                assertEquals(STATUS.CANCELED.description, request.status)
            }
            override suspend fun removeRequest(id: Int) {}
        }

        val mockNotificationRepository = object : NotificationRepository {
            override suspend fun allNotifications(): List<Notification> = emptyList()
            override suspend fun notificationByOwnerId(ownerId: Int): List<Notification> = emptyList()
            override suspend fun addNotification(notification: NewNotification): Notification = Notification(
                id = 1,
                title = "New Notification",
                body = "New Notification",
                isSend = true,
                ownerId = 1
            )
            override suspend fun updateNotification(id: Int, notification: Notification) {}
            override suspend fun removeNotification(id: Int) {}
        }




        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRepository)
                it.attributes.put(NotificationRepositoryKey, mockNotificationRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.delete("/api/requests/1/cancel")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Canceled Request"))
    }

    @Test
    fun testDoneRequest() = testApplication {
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

        val mockNotificationRepository = object : NotificationRepository {
            override suspend fun allNotifications(): List<Notification> = emptyList()
            override suspend fun notificationByOwnerId(ownerId: Int): List<Notification> = emptyList()
            override suspend fun addNotification(notification: NewNotification): Notification = Notification(
                id = 1,
                title = "New Notification",
                body = "New Notification",
                isSend = true,
                ownerId = 1
            )
            override suspend fun updateNotification(id: Int, notification: Notification) {}
            override suspend fun removeNotification(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRequestRepository)
                it.attributes.put(UserRepositoryKey, mockUserRepository)
                it.attributes.put(NotificationRepositoryKey, mockNotificationRepository)
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
    fun testTakeRequest() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = Request(
                id = 1,
                title = "Take Request",
                status = STATUS.NEW.description,
                executorUserId = 2,
                type = "people"
            )
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                executorUserId = 2
            )
            override suspend fun updateRequest(id: Int, request: Request) {
                assertEquals(STATUS.SEARCH_FOR_RESIDENT.description, request.status)
                assertEquals(2, request.executorUserId)
            }
            override suspend fun removeRequest(id: Int) {}
        }

        val mockNotificationRepository = object : NotificationRepository {
            override suspend fun allNotifications(): List<Notification> = emptyList()
            override suspend fun notificationByOwnerId(ownerId: Int): List<Notification> = emptyList()
            override suspend fun addNotification(notification: NewNotification): Notification = Notification(
                id = 1,
                title = "New Notification",
                body = "New Notification",
                isSend = true,
                ownerId = 1
            )
            override suspend fun updateNotification(id: Int, notification: Notification) {}
            override suspend fun removeNotification(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRepository)
                it.attributes.put(NotificationRepositoryKey, mockNotificationRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.put("/api/requests/1/take?user_id=2")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Take Request"))
    }

    @Test
    fun testFindRequest() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = Request(
                id = 1,
                title = "Find Request",
                status = STATUS.SEARCH_FOR_RESIDENT.description,
                executorUserId = 2,
                type = "people"
            )
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                executorUserId = 2
            )
            override suspend fun updateRequest(id: Int, request: Request) {
                assertEquals(STATUS.RESIDENT_FOUND.description, request.status)
                assertEquals("John Doe", request.people)
            }
            override suspend fun removeRequest(id: Int) {}
        }

        val mockNotificationRepository = object : NotificationRepository {
            override suspend fun allNotifications(): List<Notification> = emptyList()
            override suspend fun notificationByOwnerId(ownerId: Int): List<Notification> = emptyList()
            override suspend fun addNotification(notification: NewNotification): Notification = Notification(
                id = 1,
                title = "New Notification",
                body = "New Notification",
                isSend = true,
                ownerId = 1
            )
            override suspend fun updateNotification(id: Int, notification: Notification) {}
            override suspend fun removeNotification(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRepository)
                it.attributes.put(NotificationRepositoryKey, mockNotificationRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val findRequest = findRequestDTO(people = "John Doe")

        val response = client.put("/api/requests/1/find") {
            contentType(ContentType.Application.Json)
            setBody(findRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Find Request"))
    }

    @Test
    fun testAcceptRequest() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = Request(
                id = 1,
                title = "Accept Request",
                status = STATUS.RESIDENT_FOUND.description,
                executorUserId = 2,
                type = "people"
            )
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                executorUserId = 2
            )
            override suspend fun updateRequest(id: Int, request: Request) {
                assertEquals(STATUS.ACCEPTED.description, request.status)
                assertEquals(2, request.executorUserId)
            }
            override suspend fun removeRequest(id: Int) {}
        }

        val mockNotificationRepository = object : NotificationRepository {
            override suspend fun allNotifications(): List<Notification> = emptyList()
            override suspend fun notificationByOwnerId(ownerId: Int): List<Notification> = emptyList()
            override suspend fun addNotification(notification: NewNotification): Notification = Notification(
                id = 1,
                title = "New Notification",
                body = "New Notification",
                isSend = true,
                ownerId = 1
            )
            override suspend fun updateNotification(id: Int, notification: Notification) {}
            override suspend fun removeNotification(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRepository)
                it.attributes.put(NotificationRepositoryKey, mockNotificationRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.put("/api/requests/1/accept?user_id=2")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Accept Request"))
    }

    @Test
    fun testCourierRequest() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = Request(
                id = 1,
                title = "Courier Request",
                status = STATUS.ACCEPTED.description,
                executorUserId = 2,
                type = "people"
            )
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                executorUserId = 2
            )
            override suspend fun updateRequest(id: Int, request: Request) {
                assertEquals(STATUS.СOURIER.description, request.status)
                assertEquals("Courier Name", request.people)
            }
            override suspend fun removeRequest(id: Int) {}
        }

        val mockNotificationRepository = object : NotificationRepository {
            override suspend fun allNotifications(): List<Notification> = emptyList()
            override suspend fun notificationByOwnerId(ownerId: Int): List<Notification> = emptyList()
            override suspend fun addNotification(notification: NewNotification): Notification = Notification(
                id = 1,
                title = "New Notification",
                body = "New Notification",
                isSend = true,
                ownerId = 1
            )
            override suspend fun updateNotification(id: Int, notification: Notification) {}
            override suspend fun removeNotification(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRepository)
                it.attributes.put(NotificationRepositoryKey, mockNotificationRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val courierRequest = findRequestDTO(people = "Courier Name")

        val response = client.put("/api/requests/1/courier") {
            contentType(ContentType.Application.Json)
            setBody(courierRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Courier Request"))
    }

    @Test
    fun testRefuseRequest() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = Request(
                id = 1,
                title = "Refuse Request",
                status = STATUS.ACCEPTED.description,
                executorUserId = 2,
                type = "people"
            )
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                executorUserId = 2
            )
            override suspend fun updateRequest(id: Int, request: Request) {
                assertEquals(STATUS.NEW.description, request.status)
                assertEquals(null, request.executorUserId)
            }
            override suspend fun removeRequest(id: Int) {}
        }

        val mockNotificationRepository = object : NotificationRepository {
            override suspend fun allNotifications(): List<Notification> = emptyList()
            override suspend fun notificationByOwnerId(ownerId: Int): List<Notification> = emptyList()
            override suspend fun addNotification(notification: NewNotification): Notification = Notification(
                id = 1,
                title = "New Notification",
                body = "New Notification",
                isSend = true,
                ownerId = 1
            )
            override suspend fun updateNotification(id: Int, notification: Notification) {}
            override suspend fun removeNotification(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRepository)
                it.attributes.put(NotificationRepositoryKey, mockNotificationRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.put("/api/requests/1/refuse?user_id=2")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Refuse Request"))
    }

    @Test
    fun testCreateVampire() = testApplication {
        val mockRequestRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = Request(
                id = 1,
                title = "Create Vampire Request",
                status = STATUS.ACCEPTED.description,
                creatorUserId = 2,
                executorUserId = null
            )
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                creatorUserId = 2,
                executorUserId = null
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
                assertEquals("supremeVampire", user.role)
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

        val response = client.put("/api/requests/1/createVampire")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Create Vampire Request"))
    }

    @Test
    fun testHistoryRouteMissingUserId() = testApplication {
        // Настройка приложения Ktor
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


        // Отправляем запрос без параметра user_id
        val response = client.get("/api/requests/history")

        // Проверяем, что сервер вернул статус 404 и сообщение "Missing user id"
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Missing user id", response.bodyAsText())
    }

    @Test
    fun testMeetsRouteMissingUserId() = testApplication {
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

        // Отправляем запрос без параметра user_id
        val response = client.get("/api/requests/meets")

        // Проверяем, что сервер вернул статус 404 и сообщение "Missing user id"
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Missing user id", response.bodyAsText())
    }

    @Test
    fun testActiveRouteMissingUserId() = testApplication {
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

        // Отправляем запрос без параметра user_id
        val response = client.get("/api/requests/active")

        // Проверяем, что сервер вернул статус 404 и сообщение "Missing user id"
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("Missing user id", response.bodyAsText())
    }

    @Test
    fun testCourierRequestMissingId() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = null
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                executorUserId = 2
            )
            override suspend fun updateRequest(id: Int, request: Request) {
                assertEquals(STATUS.СOURIER.description, request.status)
                assertEquals("Courier Name", request.people)
            }
            override suspend fun removeRequest(id: Int) {}
        }

        val mockNotificationRepository = object : NotificationRepository {
            override suspend fun allNotifications(): List<Notification> = emptyList()
            override suspend fun notificationByOwnerId(ownerId: Int): List<Notification> = emptyList()
            override suspend fun addNotification(notification: NewNotification): Notification = Notification(
                id = 1,
                title = "New Notification",
                body = "New Notification",
                isSend = true,
                ownerId = 1
            )
            override suspend fun updateNotification(id: Int, notification: Notification) {}
            override suspend fun removeNotification(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRepository)
                it.attributes.put(NotificationRepositoryKey, mockNotificationRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val courierRequest = findRequestDTO(people = "Courier Name")

        val response = client.put("/api/requests/1/courier") {
            contentType(ContentType.Application.Json)
            setBody(courierRequest)
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("No request with id 1", response.bodyAsText())
    }

    @Test
    fun testAcceptRequestMissingId() = testApplication {
        val mockRepository = object : RequestRepository {
            override suspend fun allRequest(): List<Request> = emptyList()
            override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = emptyList()
            override suspend fun requestById(id: Int): Request? = null
            override suspend fun getActiveRequest(userId: Int, type: String?): Request? = null
            override suspend fun addRequest(request: NewRequest): Request = Request(
                id = 1,
                title = "New Request",
                type = request.type,
                status = request.status,
                executorUserId = 2
            )
            override suspend fun updateRequest(id: Int, request: Request) {
                assertEquals(STATUS.ACCEPTED.description, request.status)
                assertEquals(2, request.executorUserId)
            }
            override suspend fun removeRequest(id: Int) {}
        }

        val mockNotificationRepository = object : NotificationRepository {
            override suspend fun allNotifications(): List<Notification> = emptyList()
            override suspend fun notificationByOwnerId(ownerId: Int): List<Notification> = emptyList()
            override suspend fun addNotification(notification: NewNotification): Notification = Notification(
                id = 1,
                title = "New Notification",
                body = "New Notification",
                isSend = true,
                ownerId = 1
            )
            override suspend fun updateNotification(id: Int, notification: Notification) {}
            override suspend fun removeNotification(id: Int) {}
        }

        application {
            configureRouting()
            // Заменяем репозиторий на мок
            environment.monitor.subscribe(ApplicationStarted) {
                it.attributes.put(RequestRepositoryKey, mockRepository)
                it.attributes.put(NotificationRepositoryKey, mockNotificationRepository)
            }
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.put("/api/requests/1/accept?user_id=2")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("No request with id 1", response.bodyAsText())
    }

}
