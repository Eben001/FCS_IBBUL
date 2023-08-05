package com.ebenezer.gana.fcsibbul.data.repository.announcement

import android.util.Log
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.data.models.User
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber


class AnnouncementDetailsRepository(private val firestore: FirebaseFirestore) {

    fun getUpdatedLikedUsers(
        announcementId: String,
        likedUsers: (ArrayList<String>) -> Unit
    ) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .document(announcementId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.e("Unable to get liked users: Listen Failed", error)
                    return@addSnapshotListener
                }
                val document = value?.toObject(Announcement::class.java)
                if (document != null) {
                    likedUsers(document.likedBy)
                }
            }
    }


    fun addLike(likedUser: String, documentId: String) {
        val fieldUpdate = mapOf<String, Any>("likedBy" to FieldValue.arrayUnion(likedUser))
        updateDocument(documentId, fieldUpdate)
    }

    fun updateLikeMap(updateMap: Map<String, Any>, documentId: String) {
        updateDocument(documentId, updateMap)
    }

    fun removeLike(likedBy: String, documentId: String) {
        val fieldUpdate = mapOf<String, Any>("likedBy" to FieldValue.arrayRemove(likedBy))
        updateDocument(documentId, fieldUpdate)
    }


    private fun updateDocument(documentId: String, fieldUpdate: Map<String, Any>) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .document(documentId)
            .update(fieldUpdate)
            .apply {
                addOnSuccessListener {
                    Timber.d("Update successful")
                }
                addOnFailureListener { exception ->
                    Timber.e("Update failed", exception)
                }
            }
    }

    fun getUpdatedLikes(announcementId: String, likes: (Long) -> Unit) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .document(announcementId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.e("Unable to get updated likes: Listen Failed", error)
                    return@addSnapshotListener
                }
                val document = value?.toObject(Announcement::class.java)
                if (document != null) {
                    likes(document.likeCount)
                }
            }
    }
    fun deleteAnnouncement(
        announcementId: String,
        onSuccess: (UiText) -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .document(announcementId)
            .delete()
            .addOnSuccessListener {
                onSuccess(UiText.StringResource(R.string.success_delete))
            }
            .addOnFailureListener {
                onFailure(UiText.DynamicString(it.localizedMessage!!))
            }

    }

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
                    val document = value.toObject(User::class.java)
                    user(document!!)

                }

            }
    }

    /**
     * Gets the current logged in user id
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