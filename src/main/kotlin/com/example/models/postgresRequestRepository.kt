package com.example.models


import com.example.models.Request
import com.example.db.RequestDAO
import com.example.db.RequestTable
import com.example.db.daoToModelRequest
import com.example.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.example.plugins.STATUS
import org.jetbrains.exposed.sql.*

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.Op



class PostgresRequestRepository : RequestRepository {
     override suspend fun allRequest(): List<Request> = suspendTransaction {
         RequestDAO.all().map(::daoToModelRequest)
    }

    override suspend fun allRequestFilter(filter: String?, userId: Int?): List<Request> = suspendTransaction {
        val query = when (filter) {
            null -> {
                // Запрос для случая, когда filter отсутствует
                RequestDAO.find{(RequestTable.executorUserId.isNull()) and
                        (RequestTable.status eq STATUS.NEW.description) and
                        (RequestTable.type neq "meet")}
            }
            "history" -> {
                // Запрос для filter = 'history'
                RequestDAO.find{
                    (RequestTable.executorUserId eq userId) or
                            (RequestTable.creatorUserId eq userId)
                }
            }
            "meets" -> {
                // Запрос для filter = 'meets'
                RequestDAO.find {
                    (RequestTable.executorUserId.isNull()) and
                            (RequestTable.status eq STATUS.NEW.description) and
                            (RequestTable.type eq "meet")
                }
            }
            else -> {
                // Если filter не распознан, возвращаем пустой список
                return@suspendTransaction emptyList()
            }
        }
        query.limit(1000).map(::daoToModelRequest)
    }

    override suspend fun requestById(id: Int): Request? = suspendTransaction {
        RequestDAO
            .find({ RequestTable.id eq id})
            .limit(1)
            .map(::daoToModelRequest)
            .firstOrNull()
    }

    override suspend fun getActiveRequest(userId: Int, type: String?): Request? = suspendTransaction {
        RequestDAO
            .find {
                (( (RequestTable.creatorUserId eq userId) or (RequestTable.executorUserId eq userId)) and (RequestTable.status notInList listOf(STATUS.DONE.description, STATUS.CANCELED.description)))  and
                (type?.let { (RequestTable.type eq it) or (RequestTable.executorUserId.isNotNull()) } ?: Op.TRUE)
            }
            .limit(1)
            .map(::daoToModelRequest)
            .firstOrNull()
    }

    override suspend fun addRequest(request: NewRequest ): Request = suspendTransaction {
        RequestDAO.new {
            creatorUserId = request.creatorUserId
            status = request.status
            mapPoint = request.mapPoint
            comment = request.comment
            type = request.type
        }.toRequest()
    }

    override suspend fun updateRequest(id: Int, request: Request): Unit = suspendTransaction {
        RequestDAO.findByIdAndUpdate(id) {
            it.title = request.title
            it.people = request.people
            it.comment = request.comment
            it.creatorUserId = request.creatorUserId
            it.executorUserId = request.executorUserId
            it.type = request.type
            it.status = request.status
            it.mapPoint = request.mapPoint
            it.review = request.review
        }

    }

     override suspend fun removeRequest(id: Int): Unit = suspendTransaction {
         RequestTable.deleteWhere { RequestTable.id.eq(id) }
    }
}