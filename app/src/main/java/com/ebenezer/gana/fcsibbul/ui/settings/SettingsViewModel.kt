package com.ebenezer.gana.fcsibbul.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.FeedbackData
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository
import com.ebenezer.gana.fcsibbul.utils.UiText
import kotlinx.coroutines.launch


class SettingsViewModel constructor(private val repository: FcsRepository) : ViewModel() {

    private var _isFeedbackSubmitSuccess = MutableLiveData<Boolean>()
    val isFeedbackSubmitSuccess: LiveData<Boolean> = _isFeedbackSubmitSuccess

    private var _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result
    fun sendFeedback(feedbackData: FeedbackData) {
        viewModelScope.launch {
            repository.sendFeedback(feedbackData, onSuccess = {
                _isFeedbackSubmitSuccess.value = true
                _result.value = UiText.StringResource(R.string.feedback_submit_success)
            }, onFailure = {
                _isFeedbackSubmitSuccess.value = false
                _result.value = UiText.DynamicString(it)

            })
        }
    }


}