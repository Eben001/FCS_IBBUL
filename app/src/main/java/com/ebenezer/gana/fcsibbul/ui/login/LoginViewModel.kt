package com.ebenezer.gana.fcsibbul.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseUser


class LoginViewModel (private val repository: FcsRepository) : ViewModel() {

    private var _isPostSuccess = MutableLiveData<Boolean>()
    val isPostSuccess: LiveData<Boolean> = _isPostSuccess

    private var _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result

    private var _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user

    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password, { _user.value = it }, {},
            onSuccess = {
                _isPostSuccess.value = true
                _result.value = it
            },
            onFailure = {
                _isPostSuccess.value = false
                _result.value = it
            })
    }

}