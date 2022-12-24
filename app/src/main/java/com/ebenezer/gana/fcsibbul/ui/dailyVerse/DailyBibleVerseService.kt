package com.ebenezer.gana.fcsibbul.ui.dailyVerse

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.ebenezer.gana.fcsibbul.ui.common.NotificationHelper
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
class DailyBibleVerseService: Service() {
    companion object {
        private const val REQUEST_CODE = 0

        private const val BAR_TAG = "tag:Foreground_notification_bar"
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Service is started")

        NotificationHelper.startForegroundNotification(
            this, BAR_TAG, "Aeyesafe", "Foreground Service"
        )

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.d("Service is destroyed")

        NotificationHelper.cancelNotificationByTag(this, BAR_TAG)
        stopForeground(true)
    }

}