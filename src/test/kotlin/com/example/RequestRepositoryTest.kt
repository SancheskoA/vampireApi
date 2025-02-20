package com.example

import com.example.db.*
import com.example.models.*
import com.example.plugins.STATUS

import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class RequestRepositoryTest {

    @BeforeTest
    fun setup() {
        // Настройка in-memory базы данных H2
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        // Очищаем таблицы и создаем их заново перед каждым тестом
        transaction {
            SchemaUtils.drop(RequestTable) // Очищаем таблицу
            SchemaUtils.create(RequestTable) // Создаем таблицу заново
        }
    }

    @Test
    fun testAllRequests() = testApplication {
        // Создаем репозиторий
        val repository = PostgresRequestRepository()

        // Проверяем, что список запросов пуст
        val requests = repository.allRequest()
        assertTrue(requests.isEmpty())
    }

    @Test
    fun testAddRequest() = testApplication {

        // Создаем репозиторий
        val repository = PostgresRequestRepository()

        // Создаем новый запрос
        val newRequest = NewRequest(
            creatorUserId = 1,
            status = "NEW",
            mapPoint = "0,0",
            comment = "Test Comment",
            type = "vampire"
        )

        // Добавляем запрос
        val request = repository.addRequest(newRequest)

        // Проверяем, что запрос добавлен
        assertEquals("Test Comment", request.comment)
        assertEquals("vampire", request.type)
        assertEquals("NEW", request.status)
    }

    @Test
    fun testRequestById() = testApplication {


        // Создаем репозиторий
        val repository = PostgresRequestRepository()

        // Создаем новый запрос
        val newRequest = NewRequest(
            creatorUserId = 1,
            status = "NEW",
            mapPoint = "0,0",
            comment = "Test Comment",
            type = "vampire"
        )

        // Добавляем запрос
        val addedRequest = repository.addRequest(newRequest)

        // Получаем запрос по ID
        val request = repository.requestById(addedRequest.id)

        // Проверяем, что запрос найден
        assertNotNull(request)
        assertEquals("Test Comment", request.comment)
        assertEquals("vampire", request.type)
        assertEquals("NEW", request.status)
    }

    @Test
    fun testUpdateRequest() = testApplication {

        // Создаем репозиторий
        val repository = PostgresRequestRepository()

        // Создаем новый запрос
        val newRequest = NewRequest(
            creatorUserId = 1,
            status = "NEW",
            mapPoint = "0,0",
            comment = "Test Comment",
            type = "vampire"
        )

        // Добавляем запрос
        val addedRequest = repository.addRequest(newRequest)

        // Обновляем запрос
        val updatedRequest = addedRequest.copy(comment = "Updated Comment")
        repository.updateRequest(updatedRequest.id, updatedRequest)

        // Получаем обновленный запрос
        val request = repository.requestById(updatedRequest.id)

        // Проверяем, что запрос обновлен
        assertNotNull(request)
        assertEquals("Updated Comment", request.comment)
    }

    @Test
    fun testRemoveRequest() = testApplication {

        // Создаем репозиторий
        val repository = PostgresRequestRepository()

        // Создаем новый запрос
        val newRequest = NewRequest(
            creatorUserId = 1,
            status = "NEW",
            mapPoint = "0,0",
            comment = "Test Comment",
            type = "vampire"
        )

        // Добавляем запрос
        val addedRequest = repository.addRequest(newRequest)

        // Удаляем запрос
        repository.removeRequest(addedRequest.id)

        // Проверяем, что запрос удален
        val request = repository.requestById(addedRequest.id)
        assertNull(request)
    }

    @Test
    fun testAllRequestFilter() = testApplication {

        // Создаем репозиторий
        val repository = PostgresRequestRepository()

        // Создаем несколько запросов
        val request1 = repository.addRequest(
            NewRequest(
                creatorUserId = 1,
                status = "NEW",
                mapPoint = "0,0",
                comment = "Request 1",
                type = "vampire"
            )
        )
        val request2 = repository.addRequest(
            NewRequest(
                creatorUserId = 2,
                status = STATUS.NEW.description,
                mapPoint = "1,1",
                comment = "Request 2",
                type = "meet"
            )
        )

        // Проверяем фильтр для "history"
        val historyRequests = repository.allRequestFilter("history", 1)
        assertEquals(1, historyRequests.size)
        assertEquals("Request 1", historyRequests[0].comment)

        // Проверяем фильтр для "meets"
        val meetRequests = repository.allRequestFilter("meets", null)
        assertEquals(1, meetRequests.size)
        assertEquals("Request 2", meetRequests[0].comment)

        // Проверяем фильтр для "meets"
        val nullFilterRequests = repository.allRequestFilter(null, null)
        assertTrue(nullFilterRequests.isEmpty())

    }

    @Test
    fun testGetActiveRequest() = testApplication {

        // Создаем репозиторий
        val repository = PostgresRequestRepository()

        // Создаем активный запрос
        val activeRequest = repository.addRequest(
            NewRequest(
                creatorUserId = 1,
                status = "NEW",
                mapPoint = "0,0",
                comment = "Active Request",
                type = "vampire"
            )
        )

        // Получаем активный запрос
        val request = repository.getActiveRequest(1, "vampire")

        // Проверяем, что запрос найден
        assertNotNull(request)
        assertEquals("Active Request", request.comment)
    }
}