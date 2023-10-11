package com.fcsibbul.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fcsibbul.data.repository.FcsRepository
import com.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseUser

//Not being used at the moment. We are using google sign in functionality
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