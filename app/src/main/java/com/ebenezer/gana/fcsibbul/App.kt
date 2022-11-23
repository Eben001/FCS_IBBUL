package com.ebenezer.gana.fcsibbul

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.ebenezer.gana.fcsibbul.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@RequiresApi(Build.VERSION_CODES.M)
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                networkModule,
                repositoryModule,
                storageModule, activityModule
            )
        }
    }
}