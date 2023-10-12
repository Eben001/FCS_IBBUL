package com.fcsibbul.data.models

import com.google.errorprone.annotations.Keep

@Keep
data class Song(
    val posterId: String = "",
    val title:String = "",
    val content: String = ""
)