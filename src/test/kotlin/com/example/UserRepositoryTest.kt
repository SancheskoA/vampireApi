package com.example

import com.example.db.*
import com.example.models.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class UserRepositoryTest {

    @BeforeTest
    fun setup() {
        // Настройка in-memory базы данных H2
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

        // Очищаем таблицы и создаем их заново перед каждым тестом
        transaction {
            SchemaUtils.drop(UserTable) // Очищаем таблицу
            SchemaUtils.create(UserTable) // Создаем таблицу заново
        }
    }

    @Test
    fun testAllUsers() = testApplication {
        // Создаем репозиторий
        val repository = PostgresUserRepository()

        // Проверяем, что список пользователей пуст
        val users = repository.allUsers()
        assertTrue(users.isEmpty())
    }

    @Test
    fun testAddUser() = testApplication {
        // Создаем репозиторий
        val repository = PostgresUserRepository()

        // Создаем нового пользователя
        val newUser = NewUser(
            username = "testUser",
            password = "testPass",
            role = "user",
            inviterUserId = null,
            invitationCode = "1234",
            kpi = 0
        )

        // Добавляем пользователя
        val user = repository.addUser(newUser)

        // Проверяем, что пользователь добавлен
        assertEquals("testUser", user.username)
        assertEquals("testPass", user.password)
        assertEquals("user", user.role)
        assertEquals("1234", user.invitationCode)
        assertEquals(0, user.kpi)
        assertEquals(null, user.inviterUserId)
    }

    @Test
    fun testUserById() = testApplication {
        // Создаем репозиторий
        val repository = PostgresUserRepository()

        // Создаем нового пользователя
        val newUser = NewUser(
            username = "testUser",
            password = "testPass",
            role = "user",
            inviterUserId = null,
            invitationCode = "1234",
            kpi = 0
        )

        // Добавляем пользователя
        val addedUser = repository.addUser(newUser)

        // Получаем пользователя по ID
        val user = repository.userById(addedUser.id)

        // Проверяем, что пользователь найден
        assertNotNull(user)
        assertEquals("testUser", user.username)
        assertEquals("testPass", user.password)
        assertEquals("user", user.role)
        assertEquals("1234", user.invitationCode)
        assertEquals(0, user.kpi)
        assertEquals(null, user.inviterUserId)
    }

    @Test
    fun testUserByUserName() = testApplication {
        // Создаем репозиторий
        val repository = PostgresUserRepository()

        // Создаем нового пользователя
        val newUser = NewUser(
            username = "testUser",
            password = "testPass",
            role = "user",
            inviterUserId = null,
            invitationCode = "1234",
            kpi = 0
        )

        // Добавляем пользователя
        repository.addUser(newUser)

        // Получаем пользователя по имени
        val user = repository.userByUserName("testUser")

        // Проверяем, что пользователь найден
        assertNotNull(user)
        assertEquals("testUser", user.username)
        assertEquals("testPass", user.password)
        assertEquals("user", user.role)
        assertEquals("1234", user.invitationCode)
        assertEquals(0, user.kpi)
        assertEquals(null, user.inviterUserId)
    }

    @Test
    fun testUserByInvitationCode() = testApplication {
        // Создаем репозиторий
        val repository = PostgresUserRepository()

        // Создаем нового пользователя
        val newUser = NewUser(
            username = "testUser",
            password = "testPass",
            role = "user",
            inviterUserId = null,
            kpi = 0,
            invitationCode = "1234"
        )

        // Добавляем пользователя
        repository.addUser(newUser)

        // Получаем пользователя по коду приглашения
        val user = repository.userByInvitationCode("1234")

        // Проверяем, что пользователь найден
        assertNotNull(user)
        assertEquals("testUser", user.username)
        assertEquals("testPass", user.password)
        assertEquals("user", user.role)
        assertEquals("1234", user.invitationCode)
        assertEquals(0, user.kpi)
        assertEquals(null, user.inviterUserId)
    }

    @Test
    fun testUpdateUser() = testApplication {
        // Создаем репозиторий
        val repository = PostgresUserRepository()

        // Создаем нового пользователя
        val newUser = NewUser(
            username = "testUser",
            password = "testPass",
            role = "user",
            inviterUserId = null,
            invitationCode = "1234",
            kpi = 0
        )

        // Добавляем пользователя
        val addedUser = repository.addUser(newUser)

        // Обновляем пользователя
        val updatedUser = addedUser.copy(username = "updatedUser", kpi = 10)
        repository.updateUser(updatedUser.id, updatedUser)

        // Получаем обновленного пользователя
        val user = repository.userById(updatedUser.id)

        // Проверяем, что пользователь обновлен
        assertNotNull(user)
        assertEquals("updatedUser", user.username)
        assertEquals(10, user.kpi)
    }

    @Test
    fun testRemoveUser() = testApplication {
        // Создаем репозиторий
        val repository = PostgresUserRepository()

        // Создаем нового пользователя
        val newUser = NewUser(
            username = "testUser",
            password = "testPass",
            role = "user",
            inviterUserId = null,
            invitationCode = "1234",
            kpi = 0
        )

        // Добавляем пользователя
        val addedUser = repository.addUser(newUser)

        // Удаляем пользователя
        repository.removeUser(addedUser.id)

        // Проверяем, что пользователь удален
        val user = repository.userById(addedUser.id)
        assertNull(user)
    }
}