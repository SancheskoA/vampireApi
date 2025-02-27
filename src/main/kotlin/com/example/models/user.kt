package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val username: String,
    val password: String,
    val role: String,
    val inviterUserId: Int?,
    val invitationCode: String,
    val kpi: Int
)

@Serializable
data class User(
    val id: Int,
    val username: String,
    var password: String,
    var role: String,
    val invitationCode: String,
    var kpi: Int,
    val inviterUserId: Int?
)

@Serializable
data class LoginDTO(val login: String, val password: String)

@Serializable
data class RegistrationDTO(val login: String, val password: String, val code: String)

@Serializable
data class passwordDTO(val oldPassword: String, val newPassword: String)
