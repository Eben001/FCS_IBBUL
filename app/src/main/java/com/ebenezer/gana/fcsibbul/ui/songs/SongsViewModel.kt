package com.ebenezer.gana.fcsibbul.ui.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.models.Song
import com.ebenezer.gana.fcsibbul.data.repository.song.SongRepository


class SongsViewModel constructor(private val repository:SongRepository) : ViewModel() {
    private val _song = MutableLiveData<Song>()
    val song:LiveData<Song> = _song


    fun getSong(){
        repository.getSong {
            _song.value = it
        }
    }
}