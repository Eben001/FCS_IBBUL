package com.ebenezer.gana.fcsibbul.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exco(
    val id:String = "",
    val name:String = "",
    val image_url:String = "",
    val phone:String = "",
    val post:String = "",
    val department:String = "",
    val level:String = ""
):Parcelable