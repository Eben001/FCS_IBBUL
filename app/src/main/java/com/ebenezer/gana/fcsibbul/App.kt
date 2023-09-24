package com.ebenezer.gana.fcsibbul

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.ebenezer.gana.fcsibbul.di.*
import com.ebenezer.gana.fcsibbul.ui.common.NotificationHelper
import com.ebenezer.gana.fcsibbul.ui.common.Prefs
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        val prefs = Prefs(this).Settings()
        val theme = prefs.theme
        prefs.bottomBar = 0

        AppCompatDelegate.setDefaultNightMode(theme)

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
        createNotificationChannels()
        initNotificationHelper()
       //startNotificationForegroundService()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNotificationHelper() {
        NotificationHelper.init(applicationContext)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val announcementChannel = NotificationChannel(
                getString(R.string.announcement_notification_channel),
                "Announcements",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Announcement notifications"
            }
            val bibleVerseChannel = NotificationChannel(
                getString(R.string.bible_verse_notification_channel),
                "Daily Bible Verse",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily Bible Verse notifications"
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(
                listOf(
                    announcementChannel,
                    bibleVerseChannel
                )
            )
        }

    }
}