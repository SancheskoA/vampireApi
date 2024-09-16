package com.example.db

import com.example.models.NewProduct
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


import com.example.models.Product
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.idParam
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ProductTable : IntIdTable("product") {
    val name = varchar("name", 50)
    val quantity = integer( "quantity")
    val brand = varchar("brand", 50)

}

class ProductDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(ProductTable)

    var name by ProductTable.name
    var quantity by ProductTable.quantity
    var brand by ProductTable.brand
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)


fun daoToModel(dao: ProductDAO) = Product(
    dao.id.value,
    dao.name,
    dao.quantity,
    dao.brand,
)


