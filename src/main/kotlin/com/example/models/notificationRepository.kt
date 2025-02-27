package com.example.models
import io.ktor.util.AttributeKey

interface NotificationRepository {
    suspend fun allNotifications(): List<Notification>
    suspend fun notificationByOwnerId(ownerId: Int): List<Notification>
    suspend fun addNotification(notification: NewNotification): Notification
    suspend fun updateNotification(id: Int, notification: Notification)
    suspend fun removeNotification(id: Int)
}

val NotificationRepositoryKey = AttributeKey<NotificationRepository>("NotificationRepository")
