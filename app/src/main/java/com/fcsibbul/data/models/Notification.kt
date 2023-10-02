package com.fcsibbul.data.models



data class FCMNotification(
    val to:String,
    val notification: Notification
)
data class Notification(
    val title: String,
    val body: String,
    val channelId: String
)