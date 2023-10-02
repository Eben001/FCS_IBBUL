package com.fcsibbul.data.repository.bibleVerse

import com.fcsibbul.R
import com.fcsibbul.data.models.DailyBibleVerse
import com.fcsibbul.data.models.FCMNotification
import com.fcsibbul.data.models.Notification
import com.fcsibbul.data.remote.NotificationAPI
import com.fcsibbul.utils.Constants
import com.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import timber.log.Timber

class BibleVerseRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val notificationAPI: NotificationAPI
) {

    fun getDailyBibleVerse(bibleVerse: (DailyBibleVerse) -> Unit) {
        firestore.collection(Constants.BIBLE_VERSE)
            .document(Constants.BIBLE_VERSE)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.d("getBibleVerse: Listen Failed", error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val document = value.toObject(DailyBibleVerse::class.java)
                    bibleVerse(document!!)


                }
            }
    }

    /**
     * Posts a new bible verse
     */
    fun postDailyBibleVerse(
        newDailyBibleVerse: DailyBibleVerse,
        onSuccess: (UiText) -> Unit, onFailure: (UiText) -> Unit
    ) {
        var currentUserId = ""
        firebaseAuth.currentUser?.let {
            currentUserId = it.uid
        }
        val dailyBibleVerse = DailyBibleVerse(
            posterId = currentUserId,
            content = newDailyBibleVerse.content
        )
        firestore.collection(Constants.BIBLE_VERSE)
            .document(Constants.BIBLE_VERSE)
            .set(dailyBibleVerse, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess(UiText.StringResource(R.string.success))
            }
            .addOnFailureListener {
                onFailure(UiText.DynamicString(it.message!!))
            }
    }

    suspend fun sendNotification(to: String, title: String, body: String, channelId:String) {
        val apiKey =
            "AAAAcij1_Bs:APA91bEZktqy7YzM_2cRT_LLnh3OOkilswxr2h6ZhTK1e1wAm494HKB3IS0-bz5KfV7_k6gF2C_y8QsvAy-i2ty9ItjrapfCxAQYN7IRJG36BwmsfWfCnI7xbZdlzv8NiGmm6kcE8mWf"
        val notification = FCMNotification(
            to = to,
            notification = Notification(title, body, channelId)
        )
         val response = notificationAPI.sendNotification("key=$apiKey", notification)
         if (response.isSuccessful) {
             // Notification sent successfully
             Timber.d("Success Sending Notification: ${response.body()}")
         } else {
             // Error sending notification
             Timber.d("Failed to send notification: ${response.errorBody()}")

         }
    }


/*
    suspend fun sendNotification(to: String,body: String) {
        val url = URL("")
        val notification = HashMap<String, String>()
        notification["to"] = to
        notification["body"] = body

        */
/*val data = HashMap<String, String>()
        data["key1"] = "value1"
        data["key2"] = "value2"*//*


        */
/*val message = HashMap<String, Any>()
        message["to"] = to
        message["notification"] = notification
*//*

        val json = """
    {
        "to": "/topics/$to",
        "data": $notification
    }
    """

        withContext(Dispatchers.IO) {
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "AAAAcij1_Bs:APA91bEZktqy7YzM_2cRT_LLnh3OOkilswxr2h6ZhTK1e1wAm494HKB3IS0-bz5KfV7_k6gF2C_y8QsvAy-i2ty9ItjrapfCxAQYN7IRJG36BwmsfWfCnI7xbZdlzv8NiGmm6kcE8mWf")
            connection.doOutput = true
            connection.doInput = true

            val outputStream = connection.outputStream
            outputStream.write(json.toByteArray(charset("UTF-8")))
            outputStream.close()

            val responseMessage = connection.responseMessage
            Timber.d("Response Message: $responseMessage")

        }
    }
*/


}