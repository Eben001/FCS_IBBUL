package com.fcsibbul.data.models

import com.google.errorprone.annotations.Keep

@Keep
data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String? = "",
    val mobile: Long = 0,
    val gender: String = "",
    val role:Int = 0
)