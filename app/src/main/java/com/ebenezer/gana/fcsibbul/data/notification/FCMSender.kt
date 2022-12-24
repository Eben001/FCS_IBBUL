package com.ebenezer.gana.fcsibbul.data.notification

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class FCMSender {
    private var client: OkHttpClient = OkHttpClient()

    /*
     * Method to send notification to the application
     * */
    fun send(message: String?, callback: Callback?) {
        message?.let { it ->
            val reqBody: RequestBody = it
                .toRequestBody(MEDIA_TYPE)
            val request: Request? = Request.Builder()
                .url(FCM_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", KEY_STRING)
                .post(reqBody)
                .build()
            val call: Call? = request?.let {
                client.newCall(it) }
            if (callback != null) {
                call?.enqueue(callback)
            }
        }
    }

    companion object {
        /*
     * URL where we request to send notification and the key to send notification using admin sdk
     * */
        private const val FCM_URL = "https://fcm.googleapis.com/fcm/send"
        private const val KEY_STRING = "key=AAAAcij1_Bs:APA91bEZktqy7YzM_2cRT_LLnh3OOkilswxr2h6ZhTK1e1wAm494HKB3IS0-bz5KfV7_k6gF2C_y8QsvAy-i2ty9ItjrapfCxAQYN7IRJG36BwmsfWfCnI7xbZdlzv8NiGmm6kcE8mWf"
        private val MEDIA_TYPE: MediaType = "application/json".toMediaType()

    }
}