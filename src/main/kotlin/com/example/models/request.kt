package com.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewRequest(
    var creatorUserId: Int? = null,
    var status: String? = null,
    val mapPoint: String? = null,
    val comment: String?,
    val type: String?
)

@Serializable
data class Request(
    val id: Int,
    val title: String? = null,
    var people: String? = null,
    val comment: String? = null,
    val creatorUserId: Int? = null,
    var executorUserId: Int?,
    val type: String? = null,
    var status: String? = null,
    val mapPoint: String? = null,
    var review: String? = null
)


@Serializable
data class findRequestDTO(val people: String)

@Serializable
data class reviewRequestDTO(val review: String)