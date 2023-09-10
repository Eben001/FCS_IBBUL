package com.ebenezer.gana.fcsibbul.ui.host

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.models.HeaderImage
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository

class HostActivityLoggedInViewModel(private val repository: FcsRepository): ViewModel() {

    private val _headerImages = MutableLiveData<List<HeaderImage>>()
    val headerImages: LiveData<List<HeaderImage>> = _headerImages


    fun getHeaderImagesFromFirebaseStorage() {
        repository.getHeaderImagesFromFirebase { headerImages ->
            _headerImages.value = headerImages

        }
    }

}


