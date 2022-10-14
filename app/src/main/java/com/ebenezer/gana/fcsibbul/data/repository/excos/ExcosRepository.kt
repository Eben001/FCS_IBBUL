package com.ebenezer.gana.fcsibbul.data.repository.excos

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

private const val TAG = "ExcosRepository"

class ExcosRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) {

    fun addNewExco(exco: Exco, onSuccess: (UiText) -> Unit, onFailure: (UiText) -> Unit) {

        val newExco = Exco(
            id = getCurrentUserId(),
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
        activity: Activity, imageFileURI: Uri?, imageType: String, onSuccess: (String) -> Unit,
        onFailure: (UiText) -> Unit
    ) {

        val storageReference = firebaseStorage.reference.child(
            "$imageType ${System.currentTimeMillis()}.${
                Constants.getFileExtension(
                    activity, imageFileURI
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

    fun getExcos(excos: (MutableList<Exco>) -> Unit) {
        firestore.collection(Constants.EXCOS)
            .get()
            .addOnSuccessListener { document ->
                val excosList = mutableListOf<Exco>()

                for (item in document) {
                    val list = item.toObject(Exco::class.java)
                    excosList.add(list)
                }
                excos(excosList)
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting excos list ${it.printStackTrace()}")
            }
    }


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