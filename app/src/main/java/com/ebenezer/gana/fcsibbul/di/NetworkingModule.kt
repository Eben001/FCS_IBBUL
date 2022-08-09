package com.ebenezer.gana.fcsibbul.di

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ebenezer.gana.fcsibbul.data.network.NetworkStatusChecker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {
    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuthentication() = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.M)
    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager = context.getSystemService(ConnectivityManager::class.java)

    @Provides
    @Singleton
    fun provideNetworkStatusChecker(connectivityManager: ConnectivityManager) =
        NetworkStatusChecker(connectivityManager)
}