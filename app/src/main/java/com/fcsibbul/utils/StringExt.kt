package com.fcsibbul.utils

fun String?.asException(): Exception {
    return Exception(this ?: "Msg is null")
}

/**
 * This function is called to get a [Pair] that contains the starting and ending index of the [subText]
 * in the original text (Receiver).
 */
fun String.getSubTextRange(subText: String): Pair<Int, Int> {
    val start = this.indexOf(subText)
    val end = start + subText.length

    return start to end
}