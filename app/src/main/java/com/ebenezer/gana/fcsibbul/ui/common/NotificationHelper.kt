package com.ebenezer.gana.fcsibbul.ui.common

import android.app.*
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.utils.asException
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
class NotificationHelper {

    private lateinit var applicationContext: Context

    /**
     * Map that uses a string tag of a notification as key, and an Integer that is used to define
     * the ID of each notification as value.
     */
    private val notificationIdMap = hashMapOf<String, Int>()

    /**
     * Auto-increment Integer value that represents the unique ID of each notification object.
     */
    private var currentNotificationId = 1

    private lateinit var channelId: String

    companion object {

        private val INSTANCE: NotificationHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NotificationHelper()
        }

        /**
         * This function is called to initialize this helper class by storing the ApplicationContext
         * and creating the notification channel.
         *
         * The [context] must be an ApplicationContext. This is useful if you want to create and show
         * a notification no matter whether the Activity is in the foreground.
         */
        fun init(context: Context) {
            if (context !is Application) {
                throw "Context is not from Application".asException()
            }
            INSTANCE.applicationContext = context

            createNotificationChannel(context)
        }


        /**
         * This function is called to create a notification channel with importance setting to
         * [NotificationManager.IMPORTANCE_HIGH]. The notification channel is a prerequisite for
         * deliver notification on Android 8.0 and higher.
         *
         * Note that it is safe to call this function repeatedly, because creating an existing
         * notification channel performs no operation.
         */
        private fun createNotificationChannel(context: Context) {
            INSTANCE.createNotificationChannel(context)
        }

        fun showNotificationUsingAppContext(
            notificationTag: String,
            contentTitle: String,
            contentText: String,
            priority: Int = NotificationCompat.PRIORITY_MAX,
            contentIntent: PendingIntent
        ) {
            showNotification(
                INSTANCE.applicationContext,
                notificationTag,
                contentTitle,
                contentText,
                priority,
                contentIntent
            )
        }

        fun showNotification(
            context: Context,
            notificationTag: String,
            contentTitle: String,
            contentText: String,
            priority: Int = NotificationCompat.PRIORITY_MAX,
            contentIntent:PendingIntent
        ) {
            val notification =
                INSTANCE.buildNotification(context, contentTitle, contentText, priority, contentIntent)

            with(NotificationManagerCompat.from(context)) {
                val currentId = INSTANCE.retrieveNotificationIdByTag(notificationTag)
                notify(currentId, notification)
            }
        }

        /**
         * This function is called to show a foreground notification status bar that indicates a
         * foreground Service is active.
         *
         * [context] must be of a Service.
         */
        fun startForegroundNotification(
            context: Context,
            notificationTag: String,
            contentTitle: String,
            contentText: String,
            priority: Int = NotificationCompat.PRIORITY_MAX
        ) {
            if (context !is Service) {
                throw "Context $context is not a Service".asException()
            }

            /*val notification =*/
            /*    INSTANCE.buildNotification(context, contentTitle, contentText, priority)*/

            val currentId = INSTANCE.retrieveNotificationIdByTag(notificationTag)

            //context.startForeground(currentId, notification)
        }

        fun cancelNotificationByTag(context: Context, notificationTag: String) {
            val targetId = INSTANCE.retrieveNotificationIdByTag(notificationTag)

            cancelNotificationById(context, targetId)
        }

        fun cancelNotificationById(context: Context, notificationId: Int) {
            NotificationManagerCompat.from(context).cancel(notificationId)
        }

    }

    private fun buildNotification(
        context: Context,
        contentTitle: String,
        contentText: String,
        priority: Int,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_bible_quotes)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setContentIntent(pendingIntent)
        .setPriority(priority)
        .setStyle(NotificationCompat.BigTextStyle())
        .setCategory(Notification.CATEGORY_MESSAGE)
        .setVibrate(longArrayOf(500, 1000, 500))
        .setAutoCancel(true)
        .build()


    private fun retrieveNotificationIdByTag(tag: String): Int {
        if (!notificationIdMap.containsKey(tag)) {
            notificationIdMap[tag] = currentNotificationId++
        }
        Timber.d("Current notification ID is $currentNotificationId")

        return notificationIdMap.getValue(tag)
    }


    private fun createNotificationChannel(context: Context) {
        val id = context.getString(R.string.channel_name).also {
            channelId = it
        }
        val name = context.getString(R.string.channel_name)
        val description = context.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(id, name, importance).also {
            it.description = description
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

}