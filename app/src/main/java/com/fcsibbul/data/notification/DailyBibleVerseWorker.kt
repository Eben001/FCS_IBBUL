package com.fcsibbul.data.notification

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fcsibbul.data.network.ConnectivityObserver
import com.fcsibbul.data.network.NetworkConnectivityObserver
import com.fcsibbul.ui.dailyVerse.DailyBibleVerseService
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class DailyBibleVerseWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams), KoinComponent {
    private val networkConnectivityObserver: NetworkConnectivityObserver by inject()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        networkConnectivityObserver.observe().onEach { status ->
            when (status) {
                ConnectivityObserver.Status.Available -> startNotificationService()
                ConnectivityObserver.Status.Unavailable -> {}
                else -> {}
            }
        }

        return Result.success()
    }

    private fun startNotificationService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dailyBibleVerseIntent =
                Intent(applicationContext, DailyBibleVerseService::class.java)
            applicationContext.startService(dailyBibleVerseIntent)

        }
    }
}
