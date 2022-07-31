package com.ebenezer.gana.fcsibbul.ui.signup

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.User
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val TAG = "SignUpViewModel"
class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FcsRepository = FcsRepository()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

   /* private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> = _user*/
    private val _isSignupSuccess = MutableLiveData<Boolean>()
    val isSignupSuccess:LiveData<Boolean> = _isSignupSuccess

    private val _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result


    private fun registerUser(
        firstName: String, lastName: String, email: String, password: String
    ) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)

            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    //_user.value = firebaseAuth.currentUser
                    _isSignupSuccess.value = true
                    _result.value = UiText.StringResource(R.string.success_registration)

                    // use the registered user to create a collection called users
                    val firebaseUser = task.result!!.user!!
                    val user = User(
                        firebaseUser.uid,
                        firstName, lastName, email,
                        role = 0 // 0 for non admin, 1 for admin
                    )

                    repository.registerUser(user)


                }
            }.addOnFailureListener {
                Log.e(TAG, "registerUser: Failed to register user", )
            }
    }

    fun registerNewUser(firstName: String, lastName: String, email: String,
        password: String
    ) {
        registerUser(firstName, lastName, email, password)
    }


}