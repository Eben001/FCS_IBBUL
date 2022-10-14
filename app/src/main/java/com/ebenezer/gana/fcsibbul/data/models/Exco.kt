package com.ebenezer.gana.fcsibbul.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exco(
    val id:String = "",
    val firstName:String = "",
    val lastName:String = "",
    val emailId:String = "",
    val image_url:String = "",
    val phone:String = "",
    val office:String = "",
    val department:String ="",
    val level:String = ""
):Parcelable