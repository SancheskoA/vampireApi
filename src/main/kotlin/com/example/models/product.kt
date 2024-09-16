package com.example.models



import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
    val brand: String
)

@Serializable
data class NewProduct(
    val name: String,
    val quantity: Int,
    val brand: String
)
