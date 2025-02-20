package com.example

import com.example.models.PostgresUserRepository
import com.example.models.PostgresRequestRepository

import com.example.models.UserRepositoryKey
import com.example.models.RequestRepositoryKey

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val repository = PostgresRequestRepository()

    environment.monitor.subscribe(ApplicationStarted) {
        it.attributes.put(UserRepositoryKey, PostgresUserRepository())
        it.attributes.put(RequestRepositoryKey, PostgresRequestRepository())
    }

    configureSerialization(repository)
    configureDatabases()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}


