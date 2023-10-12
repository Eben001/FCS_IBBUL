package com.fcsibbul.data.models

import com.google.errorprone.annotations.Keep

@Keep
data class DailyBibleVerse(
    val posterId:String = "",
    val content:String = ""
)