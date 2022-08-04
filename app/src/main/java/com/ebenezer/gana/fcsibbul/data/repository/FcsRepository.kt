package com.ebenezer.gana.fcsibbul.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ebenezer.gana.fcsibbul.data.models.User
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

private const val TAG = "FcsRepository"

class FcsRepository {

    //var userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    var loggedOut: MutableLiveData<Boolean> = MutableLiveData()

    private var mFireStore = FirebaseFirestore.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private fun registerUser(user: User) {
        mFireStore.collection(Constants.USERS)
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                logoutUser()
            }
            .addOnFailureListener {
                Log.d(TAG, "registerUser: Error while registering this user")
            }
    }


    // when trying to log in, we are receiving the user details and will check
    //user has completed their profile or not. If completed, proceed to log in else prompt user to complete profile
    fun loginUser(
        email: String, password: String, user: (FirebaseUser?) -> Unit,
        userDetails: (User?) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getUserDetails {
                        userDetails(it)
                    }
                    user(firebaseAuth.currentUser)
                    loggedOut.value = false
                }
            }
    }

    private fun getUserDetails(userDetails: (User?) -> Unit) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)!!
                userDetails(user)
            }
    }


    // Returns the snapshot of a the logged in user
    fun verifyIfAdmin(user: (User) -> Unit) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d(TAG, "loginAdmin: Listen Failed", error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val document = value.toObject(User::class.java)
                    user(document!!)

                }

            }
    }


    private fun getCurrentUserId(): String {
        // An instance of currentUser using FirebaseAuth module
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun logoutUser() {
        firebaseAuth.signOut()
        loggedOut.value = true
    }

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
                    val firebaseUser = task.result!!.user!!
                    val user = User(
                        firebaseUser.uid,
                        firstName, lastName, email,
                        role = 0 // 0 for non admin role, 1 for admin
                    )
                    registerUser(user)
                }
            }.addOnFailureListener {
               onFailure(it)
            }
    }

}