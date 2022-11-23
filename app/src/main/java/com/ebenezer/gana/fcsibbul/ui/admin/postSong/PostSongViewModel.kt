package com.ebenezer.gana.fcsibbul.ui.admin.postSong

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.models.Song
import com.ebenezer.gana.fcsibbul.data.repository.song.SongRepository
import com.ebenezer.gana.fcsibbul.utils.UiText


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