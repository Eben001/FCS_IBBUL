package com.ebenezer.gana.fcsibbul.data.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.ui.common.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class FCMReceiver : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("Refreshed Token: $token")

    }
    /*
    * This is automatically called when notification is being received
    * */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {notification->
            notification.body?.let {
                Timber.d("Notification: $it")
                NotificationHelper.showNotificationUsingAppContext("fcs_notification",
                "Daily Bible Verse", it)
            }

        }

    }


    /*
* Method to show notification when received
* */
/*
    private fun showNotification(message:String) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.daily_bible_verse))
            .setSmallIcon(R.drawable.ic_bible_quotes)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.primaryColor))
        val notificationManager = NotificationManagerCompat.from(this)

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(Random.nextInt() + 1000, builder.build())
    }
*/

    /*
* Method to create notification channel
* */
/*
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            channel.setShowBadge(false)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
*/

    companion object {
        private const val CHANNEL_ID = "Notification_channel"
    }

}

