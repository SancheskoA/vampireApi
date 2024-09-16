package com.example.routes


import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productRouting() {
    val repository = PostgresProductRepository()

    route("/product") {
        get {
            val product = repository.allProducts()
            call.respond(product)
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val product =
                repository.productById(id.toInt()) ?: return@get call.respondText(
                    "No product with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(product)

        }
        post {
            val product = call.receive<NewProduct>()

            repository.addProduct(product)
            call.respondText("Product stored correctly", status = HttpStatusCode.Created)

        }
        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val product =
                repository.productById(id.toInt()) ?: return@put call.respondText(
                    "No product with id $id",
                    status = HttpStatusCode.NotFound
                )
            val newProduct = call.receive<NewProduct>()
            repository.updateProduct(id.toInt(),newProduct)
            call.respondText("Product updated correctly", status = HttpStatusCode.Created)

        }

        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val product =
                repository.productById(id.toInt()) ?: return@delete call.respondText(
                    "No product with id $id",
                    status = HttpStatusCode.NotFound
                )
            repository.removeProduct(id.toInt())
            call.respondText("Product deleted correctly", status = HttpStatusCode.OK)
        }
    }
}