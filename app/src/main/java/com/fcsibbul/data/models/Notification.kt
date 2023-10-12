package com.fcsibbul.data.models

import com.google.errorprone.annotations.Keep


@Keep
data class FCMNotification(
    val to:String,
    val notification: Notification
)
@Keep
data class Notification(
    val title: String,
    val body: String,
    val channelId: String
)