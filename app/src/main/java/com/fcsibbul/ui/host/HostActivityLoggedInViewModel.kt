package com.fcsibbul.ui.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fcsibbul.data.models.FYBScreenSettings
import com.fcsibbul.data.models.HeaderImage
import com.fcsibbul.data.models.HeaderTag
import com.fcsibbul.data.repository.FcsRepository

class HostActivityLoggedInViewModel(private val repository: FcsRepository) : ViewModel() {

    private val _headerImages = MutableLiveData<List<HeaderImage>>()
    val headerImages: LiveData<List<HeaderImage>> = _headerImages

    private val _headerTag = MutableLiveData<HeaderTag>()
    val headerTag: LiveData<HeaderTag> = _headerTag

    private val _fybScreenSettings = MutableLiveData<FYBScreenSettings>()
    val fybScreenSettings:LiveData<FYBScreenSettings> = _fybScreenSettings

    fun getHeaderImagesFromFirebaseStorage() {
        repository.getHeaderImagesFromFirebase { headerImages ->
            _headerImages.value = headerImages

        }
    }

    fun getHeaderTag() {
        repository.getHeaderTag { headerTag ->
            _headerTag.value = headerTag
        }
    }

    fun getFybScreenSettings() {
        repository.getFybScreenSettings { fybSettings ->
            _fybScreenSettings.value = fybSettings

        }
    }


}


