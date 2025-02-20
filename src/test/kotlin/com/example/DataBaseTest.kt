package com.example
import com.example.db.*

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.server.testing.*
import kotlin.test.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DataBaseTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            Database.connect(
                "jdbc:postgresql://localhost:5432/db_test",
                user = "itmo_user",
                password = "pass"
            )
            transaction {
                SchemaUtils.drop(UserTable, RequestTable) // Очищаем таблицу
                SchemaUtils.create(UserTable, RequestTable)
            }
        }
    }


    @Test
    fun testUserInsert() {
        transaction {
            UserTable.insert {
                it[username] = "testUser"
                it[password] = "testpass"
                it[role] = "vampire"
                it[invitationCode] = "12345"
                it[kpi] = 100
                it[inviterUserId] = null
            }

            val user = UserTable.selectAll().first()
            assertEquals("testUser", user[UserTable.username])
            assertEquals("vampire", user[UserTable.role])
        }
    }

    @Test
    fun testUserUpdate() {
        transaction {
            // Вставка пользователя
            val userId = UserTable.insert {
                it[username] = "testUser"
                it[password] = "testpass"
                it[role] = "vampire"
                it[invitationCode] = "12345"
                it[kpi] = 100
                it[inviterUserId] = null
            } get UserTable.id

            // Обновление пользователя
            UserTable.update({ UserTable.id eq userId.value }) {
                it[username] = "updateduser"
            }

            // Проверка обновления
            val updatedUser = UserTable.selectAll().where { UserTable.id eq userId.value }.singleOrNull()
            assertEquals(updatedUser?.get(UserTable.username), "updateduser")
        }
    }

    @Test
    fun testUserDelete() {
        transaction {
            // Вставка пользователя
            val userId = UserTable.insert {
                it[username] = "testUser"
                it[password] = "testpass"
                it[role] = "vampire"
                it[invitationCode] = "12345"
                it[kpi] = 100
                it[inviterUserId] = null
            } get UserTable.id

            // Удаление пользователя
            UserTable.deleteWhere { UserTable.id eq userId.value }

            // Проверка удаления
            val deletedUser = UserTable.selectAll().where { UserTable.id eq userId.value }.singleOrNull()
            assertEquals(deletedUser, null)
        }
    }

    @Test
    fun testInsertRequest() {
        transaction {
            val userId = UserTable.insert {
                it[username] = "testUser"
                it[password] = "testpass"
                it[role] = "vampire"
                it[invitationCode] = "12345"
                it[kpi] = 100
                it[inviterUserId] = null
            } get UserTable.id

            RequestTable.insert {
                it[title] = "Test Request"
                it[comment] = "This is a test request"
                it[people] = "John Doe"
                it[type] = "test"
                it[status] = "new"
                it[mapPoint] = "0,0"
                it[executorUserId] = null
                it[creatorUserId] = userId.value
            }

            val request = RequestTable.selectAll().first()
            assertEquals("Test Request", request[RequestTable.title])
            assertEquals("This is a test request", request[RequestTable.comment])
            assertEquals(userId.value, request[RequestTable.creatorUserId])
        }
    }

    @Test
    fun testUpdateRequest() {
        transaction {
            val userId = UserTable.insert {
                it[username] = "testUser"
                it[password] = "testpass"
                it[role] = "vampire"
                it[invitationCode] = "12345"
                it[kpi] = 100
                it[inviterUserId] = null
            } get UserTable.id

            val requestId = RequestTable.insert {
                it[title] = "Test Request"
                it[comment] = "This is a test request"
                it[people] = "John Doe"
                it[type] = "test"
                it[status] = "new"
                it[mapPoint] = "0,0"
                it[executorUserId] = null
                it[creatorUserId] = userId.value
            } get RequestTable.id

            // Обновление пользователя
            RequestTable.update({ RequestTable.id eq requestId.value }) {
                it[status] = "done"
            }

            val requestUser = RequestTable.selectAll().where { RequestTable.id eq requestId.value }.singleOrNull()
            assertEquals(requestUser?.get(RequestTable.status), "done")
        }
    }

    @Test
    fun testDeleteRequest() {
        transaction {
            val userId = UserTable.insert {
                it[username] = "testUser"
                it[password] = "testpass"
                it[role] = "vampire"
                it[invitationCode] = "12345"
                it[kpi] = 100
                it[inviterUserId] = null
            } get UserTable.id

            val requestId = RequestTable.insert {
                it[title] = "Test Request"
                it[comment] = "This is a test request"
                it[people] = "John Doe"
                it[type] = "test"
                it[status] = "new"
                it[mapPoint] = "0,0"
                it[executorUserId] = null
                it[creatorUserId] = userId.value
            } get RequestTable.id

            // Удаление пользователя
            RequestTable.deleteWhere { RequestTable.id eq requestId.value }

            // Проверка удаления
            val requestUser = RequestTable.selectAll().where { RequestTable.id eq userId.value }.singleOrNull()
            assertEquals(requestUser, null)
        }
    }
}
