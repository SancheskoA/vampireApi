package com.example.models

interface ProductRepository {
    suspend fun allProducts(): List<Product>
    suspend fun productById(id: Int): Product?
    suspend fun productByName(name: String): Product?
    suspend fun addProduct(product: NewProduct)
    suspend fun updateProduct(id: Int, product: NewProduct)
    suspend fun removeProduct(id: Int)
}