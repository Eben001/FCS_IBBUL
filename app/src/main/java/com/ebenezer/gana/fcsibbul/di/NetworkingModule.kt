package com.ebenezer.gana.fcsibbul.di

import android.net.ConnectivityManager
import com.ebenezer.gana.fcsibbul.data.network.NetworkConnectivityObserver
import com.ebenezer.gana.fcsibbul.data.network.NetworkStatusChecker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single { FirebaseFirestore.getInstance() }
    single { FirebaseAuth.getInstance() }
    single { androidContext().getSystemService(ConnectivityManager::class.java) }
    single { NetworkStatusChecker(get()) }
    single { NetworkConnectivityObserver(get()) }
}

