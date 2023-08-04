package com.ebenezer.gana.fcsibbul.data.repository.excos

import android.app.Activity
import android.net.Uri
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.ebenezer.gana.fcsibbul.utils.Constants.PAGE_SIZE
import com.ebenezer.gana.fcsibbul.utils.UiText
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
        activity: Activity,
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
        onFailure: (UiText) -> Unit
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
                        onFailure(UiText.DynamicString(e.localizedMessage!!))
                    }
            }
            .addOnFailureListener { e ->
                onFailure(UiText.DynamicString(e.localizedMessage!!))
            }


    }

    fun getExcos() =
        firestore.collection(Constants.EXCOS)
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .limit(PAGE_SIZE.toLong())




    fun getExcos(excos: (MutableList<Exco>) -> Unit) {
        firestore.collection(Constants.EXCOS)
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { document ->
                val excosList = mutableListOf<Exco>()

                for (item in document) {
                    val excoItem = item.toObject(Exco::class.java)
                    excoItem.documentId = item.id
                    excosList.add(excoItem)
                }
                excos(excosList)
            }
            .addOnFailureListener {
                Timber.e("Error getting excos list ${it.printStackTrace()}")
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