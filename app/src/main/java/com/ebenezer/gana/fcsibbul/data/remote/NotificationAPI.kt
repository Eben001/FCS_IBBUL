package com.ebenezer.gana.fcsibbul.data.remote

import com.ebenezer.gana.fcsibbul.data.models.FCMNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NotificationAPI {
    @POST("fcm/send")
    suspend fun send(
        @Header("Authorization") authorization: String,
        @Body fcmNotification: FCMNotification
    ): Response<ResponseBody>
}