package com.ebenezer.gana.fcsibbul.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FcsRepository = FcsRepository()

    private var _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user

    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password, { _user.value = it }, {})
    }

}