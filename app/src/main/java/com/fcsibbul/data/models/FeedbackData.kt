package com.fcsibbul.data.models

import com.google.errorprone.annotations.Keep

@Keep
data class FeedbackData(
    val userId: String?,
    val feedbackText: String
)
