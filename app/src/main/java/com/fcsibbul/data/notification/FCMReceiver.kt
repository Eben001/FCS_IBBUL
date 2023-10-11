package com.fcsibbul.data.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import com.fcsibbul.ui.common.NotificationHelper
import com.fcsibbul.ui.host.HostActivityLoggedIn
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
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        vibrateDevice(this)
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
    private fun vibrateDevice(context: Context) {
        val vibrator = context.getSystemService(Vibrator::class.java)
        vibrator?.let {
            val pattern = longArrayOf(0, 400, 100, 400)

            if (Build.VERSION.SDK_INT >= 26) {
                it.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(pattern, -1)
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "Notification_channel"
    }

}

