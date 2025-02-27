package com.example.models


import com.example.models.Notification
import com.example.db.NotificationDAO
import com.example.db.NotificationTable
import com.example.db.daoToModelNotification
import com.example.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import com.example.plugins.STATUS
import org.jetbrains.exposed.sql.*

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.Op


class PostgresNotificationRepository : NotificationRepository {
     override suspend fun allNotifications(): List<Notification> = suspendTransaction {
         NotificationDAO.all().map(::daoToModelNotification)
    }

    override suspend fun notificationByOwnerId(id: Int): List<Notification> = suspendTransaction {
        NotificationDAO
            .find({ (NotificationTable.ownerId eq id) and (NotificationTable.isSend eq false)})
            .map(::daoToModelNotification)
    }

    override suspend fun addNotification(notification: NewNotification ): Notification = suspendTransaction {
        NotificationDAO.new {
            title = notification.title
            body = notification.body
            isSend = notification.isSend
            ownerId = notification.ownerId
        }.toNotification()
    }

    override suspend fun updateNotification(id: Int, notification: Notification): Unit = suspendTransaction {
        NotificationDAO.findByIdAndUpdate(id) {
            it.title = notification.title
            it.body = notification.body
            it.isSend = notification.isSend
            it.ownerId = notification.ownerId
        }

    }

     override suspend fun removeNotification(id: Int): Unit = suspendTransaction {
         NotificationTable.deleteWhere { NotificationTable.id.eq(id) }
    }
}