package com.ebenezer.gana.fcsibbul.ui.admin.postBibleVerse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.repository.bibleVerse.BibleVerseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostBibleVerseViewModel @Inject constructor(
    private val repository: BibleVerseRepository
) : ViewModel() {

}