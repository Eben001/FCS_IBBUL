package com.fcsibbul.ui.admin.postSong

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fcsibbul.data.models.Song
import com.fcsibbul.data.repository.song.SongRepository
import com.fcsibbul.utils.UiText


class PostSongViewModel(private val repository: SongRepository):ViewModel() {
    private var _isPostSuccess = MutableLiveData<Boolean>()
    val isPostSuccess: LiveData<Boolean> = _isPostSuccess

    private var _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result


    private fun postNewSong(newSong:Song){
        repository.postSong(newSong, onSuccess = {
            _isPostSuccess.value = true
            _result.value = it
        }, onFailure = {
            _isPostSuccess.value = false
            _result.value = it
        })
    }

    fun postSong(
        posterId:String,
        title:String,
        content:String
    ){
        val newData =
            Song(posterId, title, content)
        postNewSong(newData)
    }


}