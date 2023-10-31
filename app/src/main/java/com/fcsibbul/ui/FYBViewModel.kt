package com.fcsibbul.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fcsibbul.data.models.FYBImage
import com.fcsibbul.data.models.FYBScreenSettings
import com.fcsibbul.data.repository.FcsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class FYBViewModel(private val repository: FcsRepository) : ViewModel() {
    private val _fybImages = MutableLiveData<List<FYBImage>>()
    val fybImages: LiveData<List<FYBImage>> = _fybImages

    private val _fybSettings = MutableLiveData<FYBScreenSettings>()
    val fybSettings: LiveData<FYBScreenSettings> = _fybSettings

    fun getFybSettings() {
        repository.getFybScreenSettings { fybSettings ->
            _fybSettings.value = fybSettings

        }
    }

    fun getFybImageUrlsFromFirestore() {
        viewModelScope.launch {
            delay(2000)
            repository.getFybImageUrlsFromFirestore { fybImages ->
                _fybImages.value = fybImages
                Timber.d("Fyb images $fybImages")

            }
        }
    }
}