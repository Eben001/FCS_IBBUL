package com.ebenezer.gana.fcsibbul.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.WelcomeScreenImage
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class SignUpViewModel(private val repository: FcsRepository) : ViewModel() {

    private val _isSignupSuccess = MutableLiveData<Boolean>()
    val isSignupSuccess: LiveData<Boolean> = _isSignupSuccess

    private val _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result

    private val _welcomeScreenImages = MutableLiveData<List<WelcomeScreenImage>>()
    val welcomeScreenImages: LiveData<List<WelcomeScreenImage>> = _welcomeScreenImages


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

    @Deprecated("No longer required", level = DeprecationLevel.WARNING)
    fun registerNewUser(
        firstName: String, lastName: String, email: String,
        password: String
    ) {
        registerUser(firstName, lastName, email, password)
    }

    fun handleSigningTask(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            account?.let {
                firebaseAuthWithGoogle(account.idToken!!)
            }

        } else {
            _isSignupSuccess.value = false
            _result.value = task.exception?.localizedMessage?.let { UiText.DynamicString(it) }
            Timber.e(task.exception.toString())
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        repository.signInWithGoogle(credential, onSuccess = {
            _isSignupSuccess.value = true

        },
            onFailure = {
                _isSignupSuccess.value = false
                Timber.e(it)
            })
    }

    fun getWelcomeImagesFromFirebaseStorage() {
        viewModelScope.launch {
            delay(2000)
            repository.getWelcomeImagesFromFirebase { welcomeScreenImages ->
                _welcomeScreenImages.value = welcomeScreenImages

            }
        }

    }


}