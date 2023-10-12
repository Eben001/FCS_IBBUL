package com.fcsibbul.data.models

import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class Announcement(
    var userId: String? = "",
    var announcementId: String? = "",
    val title: String? = "",
    val details: String? = "",
    val date: String? = "",
    val likeCount: Long = 0L,
    val likedBy: ArrayList<String> = arrayListOf(),
    val timeStamp: Timestamp = Timestamp.now()
) : Parcelable