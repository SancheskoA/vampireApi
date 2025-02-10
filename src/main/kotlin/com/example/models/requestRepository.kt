package com.example.models

interface RequestRepository {
    suspend fun allRequest(): List<Request>
    suspend fun allRequestFilter(filter: String?, userId: Int): List<Request>
    suspend fun requestById(id: Int): Request?
    suspend fun getActiveRequest(userId: Int, type: String?): Request?
    suspend fun addRequest(request: NewRequest): Request?
    suspend fun updateRequest(id: Int, request: Request)
    suspend fun removeRequest(id: Int)
}