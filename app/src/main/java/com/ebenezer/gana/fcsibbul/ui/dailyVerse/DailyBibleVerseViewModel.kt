package com.ebenezer.gana.fcsibbul.ui.dailyVerse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.repository.bibleVerse.BibleVerseRepository


class DailyBibleVerseViewModel (private val repository: BibleVerseRepository) :
    ViewModel() {
    private val _bibleVerse = MutableLiveData<String>()
    val bibleVerse: LiveData<String> = _bibleVerse


    fun getDailyBibleVerse() {
        repository.getDailyBibleVerse {
            _bibleVerse.value = it.content
        }
    }
}