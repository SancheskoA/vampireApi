package com.example.dto

import com.example.models.User
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRegistrationDto(
    val token: String,
    val user: User
)
