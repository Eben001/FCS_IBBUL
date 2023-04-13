package com.ebenezer.gana.fcsibbul.data.notification

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.ebenezer.gana.fcsibbul.ui.common.NotificationHelper
import com.ebenezer.gana.fcsibbul.ui.host.HostActivityLoggedIn
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

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
        val intent = Intent(applicationContext, HostActivityLoggedIn::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        remoteMessage.notification?.let {notification->
            notification.title?.let {title->
                notification.body?.let {body->
                    Timber.d("Notification: $body")
                    NotificationHelper.showNotificationUsingAppContext("fcs_notification",
                        title, body, contentIntent = pendingIntent)
                }
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

