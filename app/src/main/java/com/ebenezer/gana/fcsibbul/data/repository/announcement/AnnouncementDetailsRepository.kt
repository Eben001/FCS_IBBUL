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

private const val TAG = "AnnouncementDetailsRepo"

class AnnouncementDetailsRepository(private val firestore: FirebaseFirestore) {

    fun getUpdatedLikedUsers(
        announcementId: String,
        likedUsers: (ArrayList<String>) -> Unit
    ) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .document(announcementId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d(
                        TAG,
                        "unable to get liked users: Listen Failed",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null) {
                    val document = value.toObject(Announcement::class.java)
                    if (document != null) {
                        likedUsers(document.likedBy)
                    }

                }

            }
    }


    fun addLike(likedUser: String, documentId: String) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .document(documentId)
            .update("likedBy", FieldValue.arrayUnion(likedUser))
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.e(TAG, "postAnnouncement: Error while posting announcement")
            }
    }

    fun updateLikeHasMap(updateHashMap: HashMap<String, Any>, documentId: String) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .document(documentId)
            .update(updateHashMap)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.e(TAG, "postAnnouncement: Error while posting announcement")
            }
    }

    fun removeLike(likedBy: String, documentId: String) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .document(documentId)
            .update("likedBy", FieldValue.arrayRemove(likedBy))
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.e(TAG, "postAnnouncement: Error while posting announcement")
            }
    }


    fun getUpdatedLikes(announcementId: String, announcement: (likes: Long) -> Unit) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .document(announcementId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d(
                        TAG,
                        "loginAdmin: Listen Failed",
                        error
                    )
                    return@addSnapshotListener
                }
                if (value != null) {
                    val document = value.toObject(Announcement::class.java)
                    if (document != null) {
                        announcement(document.likeCount)
                    }

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
                    Log.d(TAG, "loginAdmin: Listen Failed", error)
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