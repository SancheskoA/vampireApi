package com.example.models
import io.ktor.util.AttributeKey

interface UserRepository {
    suspend fun allUsers(): List<User>
    suspend fun userById(id: Int): User?
    suspend fun userByUserName(name: String): User?
    suspend fun userByInvitationCode(invitationCode: String): User?
    suspend fun addUser(user: NewUser): User
    suspend fun updateUser(id: Int, user: User)
    suspend fun removeUser(id: Int)
}

val UserRepositoryKey = AttributeKey<UserRepository>("UserRepository")
