package com.ebenezer.gana.fcsibbul.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository
import com.ebenezer.gana.fcsibbul.utils.UiText


private const val TAG = "SignUpViewModel"

class SignUpViewModel (private val repository: FcsRepository) : ViewModel() {

    private val _isSignupSuccess = MutableLiveData<Boolean>()
    val isSignupSuccess: LiveData<Boolean> = _isSignupSuccess

    private val _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result


    private fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        repository.createUserWithEmailAndPassword(email, password, firstName, lastName,
            onSuccess = {
                _isSignupSuccess.value = true
                _result.value = UiText.StringResource(R.string.success_registration)
            }, onFailure = {
                _isSignupSuccess.value = false
                _result.value = UiText.DynamicString(it.localizedMessage!!)
            })
    }

    fun registerNewUser(
        firstName: String, lastName: String, email: String,
        password: String
    ) {
        registerUser(firstName, lastName, email, password)
    }


}