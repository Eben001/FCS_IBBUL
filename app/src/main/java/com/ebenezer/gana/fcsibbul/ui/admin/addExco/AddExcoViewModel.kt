package com.ebenezer.gana.fcsibbul.ui.admin.addExco

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.data.repository.excos.ExcosRepository
import com.ebenezer.gana.fcsibbul.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddExcoViewModel(private val repository: ExcosRepository) : ViewModel() {



    private val _isImageUploadSuccess = MutableStateFlow<Boolean?>(null)
    val isImageUploadSuccess: StateFlow<Boolean?> = _isImageUploadSuccess
        .stateIn(
            initialValue = null,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )


    private var _isPostSuccess = MutableLiveData<Boolean>()
    val isPostSuccess: LiveData<Boolean> = _isPostSuccess

    private var _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result

    private var _imageUrl = MutableLiveData<String>()
    val imageUrl:LiveData<String> = _imageUrl

    fun addExco(
        id: String,
        documentId:String,
        firstName: String,
        lastName: String,
        emailId: String,
        imageUrl: String,
        phoneNumber: String,
        office: String,
        department:String,
        level:Int
    ) {
        val newData =
            Exco(
                id = id, documentId = documentId,
                firstName = firstName, lastName = lastName, emailId = emailId,
                image_url = imageUrl, phone = phoneNumber, office = office, department = department,
                level = level
            )
        addNewExco(newData)
    }

    private fun addNewExco(newData: Exco) {
        repository.addNewExco(newData, onSuccess = {
            _isPostSuccess.value = true
            _result.value = it
        }, onFailure = {
            _isPostSuccess.value = false
            _result.value = it
        })
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String, excoFullName:String) {
        repository.uploadExcoImageToCloud(activity, imageFileURI, imageType, excoFullName,  onSuccess = {
            _imageUrl.value = it
            _isImageUploadSuccess.value = true

        },
            onFailure = {
                _isPostSuccess.value = false
                _result.value = it
                viewModelScope.launch {
                    _isImageUploadSuccess.emit(false)
                }

            })
    }


}