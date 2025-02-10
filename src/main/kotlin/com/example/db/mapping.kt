package com.example.db

import com.example.models.NewProduct
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

import com.example.models.Product
import com.example.models.User
import com.example.models.Request

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

fun daoToModel(dao: ProductDAO) = Product(
    dao.id.value,
    dao.name,
    dao.quantity,
    dao.brand,
)

object UserTable : IntIdTable("users") {

    val username = varchar("username", 255)
    val password = varchar( "password", 255)
    val role = varchar("role", 255)
    val invitationCode = varchar("invitation_code", 255)
    val kpi = integer("kpi")
    val inviterUserId = integer("inviter_user_id").nullable()

}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var username by UserTable.username
    var password by UserTable.password
    var role by UserTable.role
    var invitationCode by UserTable.invitationCode
    var kpi by UserTable.kpi
    var inviterUserId by UserTable.inviterUserId

    fun toUser(): User {
        return User(
            id = id.value, // Предполагаем, что id есть в Entity
            username = username,
            password = password,
            role = role,
            inviterUserId = inviterUserId,
            invitationCode = invitationCode,
            kpi = kpi
        )
    }
}

fun daoToModelUser(dao: UserDAO) = User(
    dao.id.value,
    dao.username,
    dao.password,
    dao.role,
    dao.invitationCode,
    dao.kpi,
    dao.inviterUserId
)

object RequestTable : IntIdTable("requests") {

    val title = varchar("title", 255).nullable()
    val people = varchar("people", 255).nullable()
    val comment = varchar("comment", 255).nullable()
    val creatorUserId = integer("creator_user_id").nullable()
    val executorUserId = integer("executor_user_id").nullable()
    val type = varchar("type", 255).nullable()
    val status = varchar("status", 255).nullable()
    val mapPoint = varchar("map_point", 255).nullable()
}

class RequestDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RequestDAO>(RequestTable)

    var title by RequestTable.title
    var people by RequestTable.people
    var comment by RequestTable.comment
    var creatorUserId by RequestTable.creatorUserId
    var executorUserId by RequestTable.executorUserId
    var type by RequestTable.type
    var status by RequestTable.status
    var mapPoint by RequestTable.mapPoint

    fun toRequest(): Request {
        return Request(
            id = id.value, // Предполагаем, что id есть в Entity
            title = title,
            people = people,
            comment = comment,
            creatorUserId = creatorUserId,
            executorUserId = executorUserId,
            type = type,
            status = status,
            mapPoint = mapPoint
        )
    }
}

fun daoToModelRequest(dao: RequestDAO) = Request(
    dao.id.value,
    dao.title,
    dao.people,
    dao.comment,
    dao.creatorUserId,
    dao.executorUserId,
    dao.type,
    dao.status,
    dao.mapPoint
)

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)





