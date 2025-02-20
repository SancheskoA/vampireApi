package com.example.dto

import com.example.models.Request
import com.example.models.User
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRequestDto (
    val order: Request? = null,
)

