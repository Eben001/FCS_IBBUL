package com.ebenezer.gana.fcsibbul.data.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exco(
    val id:String = "",
    var documentId:String = "",
    val firstName:String = "",
    val lastName:String = "",
    val emailId:String = "",
    val image_url:String = "",
    val phone:String = "",
    val office:String = "",
    val department:String ="",
    val level:Int = 100,
    val timeStamp: Timestamp = Timestamp.now()
):Parcelable