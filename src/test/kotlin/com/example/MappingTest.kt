package com.example


import com.example.db.*

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.server.testing.*
import kotlin.test.*

class MappingTest {

    @Test
    fun testUserDAOCreation() = testApplication {
        // Настройка in-memory базы данных H2
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        // Создаем таблицы
        transaction {
            SchemaUtils.create(UserTable)
        }

        // Тестируем создание пользователя
        val userDAO = transaction {
            UserDAO.new {
                username = "testUser"
                password = "testPass"
                role = "user"
                invitationCode = "1234"
                kpi = 0
                inviterUserId = null
            }
        }

        // Проверяем, что данные корректны
        assertEquals("testUser", userDAO.username)
        assertEquals("testPass", userDAO.password)
        assertEquals("user", userDAO.role)
        assertEquals("1234", userDAO.invitationCode)
        assertEquals(0, userDAO.kpi)
        assertEquals(null, userDAO.inviterUserId)
    }

    @Test
    fun testRequestDAOCreation() = testApplication {
        // Настройка in-memory базы данных H2
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        // Создаем таблицы
        transaction {
            SchemaUtils.create(UserTable, RequestTable)
        }

        // Тестируем создание запроса
        val requestDAO = transaction {
            RequestDAO.new {
                title = "Test Request"
                people = "John Doe"
                comment = "Test Comment"
                creatorUserId = 1
                executorUserId = 2
                type = "vampire"
                status = "NEW"
                mapPoint = "0,0"
            }
        }

        // Проверяем, что данные корректны
        assertEquals("Test Request", requestDAO.title)
        assertEquals("John Doe", requestDAO.people)
        assertEquals("Test Comment", requestDAO.comment)
        assertEquals(1, requestDAO.creatorUserId)
        assertEquals(2, requestDAO.executorUserId)
        assertEquals("vampire", requestDAO.type)
        assertEquals("NEW", requestDAO.status)
        assertEquals("0,0", requestDAO.mapPoint)
    }

}
