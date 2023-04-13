package com.ebenezer.gana.fcsibbul.ui.admin.postBibleVerse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.fcsibbul.data.models.DailyBibleVerse
import com.ebenezer.gana.fcsibbul.data.repository.bibleVerse.BibleVerseRepository
import com.ebenezer.gana.fcsibbul.utils.UiText
import kotlinx.coroutines.launch

class PostBibleVerseViewModel(
    private val repository: BibleVerseRepository
) : ViewModel() {
    private var _isPostSuccess = MutableLiveData<Boolean>()
    val isPostSuccess: LiveData<Boolean> = _isPostSuccess

    private var _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result

    private fun postNewVerse(newVerse: DailyBibleVerse) {
        repository.postDailyBibleVerse(newVerse, onSuccess = {
            _isPostSuccess.value = true
            _result.value = it
        }, onFailure = {
            _isPostSuccess.value = false
            _result.value = it
        })
    }

    fun postBibleVerse(
        posterId: String,
        content: String
    ) {
        val newData = DailyBibleVerse(posterId, content)
        postNewVerse(newData)
    }

    fun sendNotification(to:String, title: String, body:String, channelId:String){
        viewModelScope.launch {
            repository.sendNotification(to, title, body, channelId)
        }
    }

}