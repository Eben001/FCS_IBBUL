package com.fcsibbul.di

import com.fcsibbul.data.remote.NotificationAPI
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val remote = module {
    single { GsonBuilder().create() }
    single {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }
    single<NotificationAPI> { get<Retrofit>().create(NotificationAPI::class.java) }
}