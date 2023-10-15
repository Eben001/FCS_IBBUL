package com.fcsibbul.data.repository.excos

import android.net.Uri
import com.fcsibbul.R
import com.fcsibbul.data.models.Exco
import com.fcsibbul.data.models.User
import com.fcsibbul.utils.Constants
import com.fcsibbul.utils.Constants.PAGE_SIZE
import com.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber

class ExcosRepository(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) {

    /**
     * Returns the snapshot document of a the logged in user.
     * This information will be used to check if the user is an admin or not
     */
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

    fun addNewExco(exco: Exco, onSuccess: (UiText) -> Unit, onFailure: (UiText) -> Unit) {

        val newExco = Exco(
            id = getCurrentUserId(),
            documentId = exco.documentId,
            firstName = exco.firstName,
            lastName = exco.lastName,
            emailId = exco.emailId,
            image_url = exco.image_url,
            phone = exco.phone,
            office = exco.office,
            department = exco.department,
            level = exco.level
        )
        firestore.collection(Constants.EXCOS)
            .document()
            .set(newExco, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess(UiText.StringResource(R.string.success))
            }
            .addOnFailureListener {
                onFailure(UiText.DynamicString(it.message!!))
            }

    }

    fun uploadExcoImageToCloud(
        imageFileURI: Uri?,
        imageType: String,
        excoFullName: String,
        onSuccess: (String) -> Unit,
        onFailure: (UiText) -> Unit
    ) {

        val storageReference = firebaseStorage.reference.child(
            "$imageType $excoFullName ${System.currentTimeMillis()}.${
                Constants.getFileExtension(
                    imageFileURI
                )
            }"
        )

        storageReference.putFile(imageFileURI!!).addOnSuccessListener { snapShot ->
            // Get the downloadable url from the task snapshot
            snapShot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
                .addOnFailureListener {
                    onFailure(UiText.DynamicString(it.message!!))
                }
        }
    }

    fun deleteExcoDetails(
        excoDocumentId: String, imageUrl: String,
        onSuccess: (UiText) -> Unit,
        onFailure: (String) -> Unit
    ) {

        firestore.collection(Constants.EXCOS).document(excoDocumentId)
            .delete()
            .addOnSuccessListener {
                firebaseStorage.getReferenceFromUrl(imageUrl)
                    .delete()
                    .addOnSuccessListener {
                        onSuccess(UiText.StringResource(R.string.success_delete))
                    }
                    .addOnFailureListener { e ->
                        onFailure(e.localizedMessage!!)
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e.localizedMessage!!)
            }


    }

    fun getExcos() =
        firestore.collection(Constants.EXCOS)
            .orderBy("timeStamp", Query.Direction.ASCENDING)
            .limit(PAGE_SIZE.toLong())



    /**
     * Gets the current logged in user id. This is the user who uploaded the excos details
     */
    private fun getCurrentUserId(): String {
        var currentUserID = ""

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            currentUserID = it.uid
        }
        return currentUserID
    }
}