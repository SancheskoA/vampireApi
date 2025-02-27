package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class NewNotification(
    val title: String,
    val body: String,
    var isSend: Boolean = false,
    val ownerId: Int? = null
)

@Serializable
data class Notification(
    val id: Int,
    val title: String,
    val body: String,
    var isSend: Boolean,
    val ownerId: Int? = null
)
