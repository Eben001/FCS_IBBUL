package com.ebenezer.gana.fcsibbul.data.repository

import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.User
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import timber.log.Timber


class FcsRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    private fun registerUser(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        user.id.let {
            firestore.collection(Constants.USERS)
                .document(it)
                .set(user, SetOptions.merge())
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener {
                    Timber.d("registerUser: Error while registering this user")
                    onFailure(it.localizedMessage!!)
                }
        }
    }


    // when trying to log in, we are receiving the user details and will check
    //user has completed their profile or not. If completed, proceed to log in else prompt user to complete profile
    fun loginUser(
        email: String, password: String, user: (FirebaseUser?) -> Unit,
        userDetails: (User?) -> Unit, onSuccess: (UiText) -> Unit, onFailure: (UiText) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getUserDetails {
                        userDetails(it)
                    }
                    user(firebaseAuth.currentUser)
                    //isLoggedOut.value = false
                    onSuccess(UiText.StringResource(R.string.success))
                }
            }
            .addOnFailureListener {
                onFailure(UiText.DynamicString(it.message!!))
            }
    }

    private fun getUserDetails(userDetails: (User?) -> Unit) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                userDetails(user)
            }
    }


    // Returns the snapshot of a the logged in user
    fun verifyIfAdmin(user: (User) -> Unit) {
        firestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.d("loginAdmin: Listen Failed", error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    value.toObject(User::class.java)?.let {
                        user(it)
                    }

                }

            }
    }


    private fun getCurrentUserId(): String {
        // An instance of currentUser using FirebaseAuth module
        val currentUser = firebaseAuth.currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    @Deprecated("Now used sign in with google option")
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                    // use the registered user to create a collection called users
                    task.result.user?.let { firebaseUser ->
                        val user = User(
                            firebaseUser.uid,
                            firstName, lastName, email,
                            role = 0 // 0 for non admin role, 1 for admin
                        )
                        //registerUser(user)
                    }

                }
            }.addOnFailureListener {
                onFailure(it)
            }
    }

    fun signInWithGoogle(credential: AuthCredential, onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
                // use the registered user to create a collection called users in firestore
                task.result.user?.let { firebaseUser ->
                    Timber.d("Firebase User ${firebaseUser.email}")
                    val isNewUser = task.result?.additionalUserInfo?.isNewUser ?: true
                    if(isNewUser){
                        val user = User(
                            firebaseUser.uid,
                            email = firebaseUser.email,
                            role = 0 // 0 for non admin role, 1 for admin
                        )
                        registerUser(user, onSuccess, onFailure)
                    }

                }

            } else {
                task.exception?.let { onFailure(it.localizedMessage!!) }
            }
        }
    }

}