package com.ebenezer.gana.fcsibbul.data.notification

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ebenezer.gana.fcsibbul.ui.dailyVerse.DailyBibleVerseService

class DailyBibleVerseWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val connMgr = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val network = connMgr.activeNetwork
            val capabilities = connMgr.getNetworkCapabilities(network)
            if (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                // Start the foreground service to handle the FCM notification
                val dailyBibleVerseIntent = Intent(applicationContext, DailyBibleVerseService::class.java)
                applicationContext.startForegroundService(dailyBibleVerseIntent)
                /*val dailyBibleVerseIntent = Intent(applicationContext, DailyBibleVerseService::class.java)
                applicationContext.startForegroundService(dailyBibleVerseIntent)*/
            }
        }
        return Result.success()
    }
}
