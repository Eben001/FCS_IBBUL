package com.ebenezer.gana.fcsibbul.ui.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.repository.song.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(private val repository:SongRepository) : ViewModel() {
    private val _song = MutableLiveData<String>()
    val song:LiveData<String> = _song


    fun getSong(){
        repository.getSong {
            _song.value = it
        }
    }
}