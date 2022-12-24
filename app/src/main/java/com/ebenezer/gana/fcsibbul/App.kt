package com.ebenezer.gana.fcsibbul

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.ebenezer.gana.fcsibbul.di.*
import com.ebenezer.gana.fcsibbul.ui.common.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.M)
class App : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                networkModule,
                repositoryModule,
                storageModule, activityModule,
                remote
            )
        }
        initNotificationHelper()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNotificationHelper() {
        NotificationHelper.init(applicationContext)
    }
}