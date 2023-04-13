package com.ebenezer.gana.fcsibbul

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.ebenezer.gana.fcsibbul.di.*
import com.ebenezer.gana.fcsibbul.ui.common.NotificationHelper
import com.ebenezer.gana.fcsibbul.ui.dailyVerse.DailyBibleVerseService
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
        createNotificationChannels()
        initNotificationHelper()
       //startNotificationForegroundService()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startNotificationForegroundService() {
        val intent = Intent(this, DailyBibleVerseService::class.java)
        startService(intent)

        /*val dailyBibleVerseIntent = Intent(this, DailyBibleVerseService::class.java)
        val announcementIntent = Intent(this, AnnouncementService::class.java)

        // Start the DailyBibleVerseService
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(dailyBibleVerseIntent)
        } else {
            this.startService(dailyBibleVerseIntent)
        }

        // Start the AnnouncementService
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(announcementIntent)
        } else {
            this.startService(announcementIntent)
        }*/

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