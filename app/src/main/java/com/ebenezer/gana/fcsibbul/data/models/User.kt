package com.ebenezer.gana.fcsibbul.data.models

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String? = "",
    val mobile: Long = 0,
    val gender: String = "",
    val role:Int = 0
)