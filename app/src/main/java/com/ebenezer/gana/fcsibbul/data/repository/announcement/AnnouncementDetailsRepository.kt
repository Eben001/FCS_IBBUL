package com.ebenezer.gana.fcsibbul.data.repository.announcement

import android.util.Log
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "AnnouncementDetailsRepo"

class AnnouncementDetailsRepository {
    private var mFireStore = FirebaseFirestore.getInstance()

    fun getUpdatedLikedUsers(
        announcementId: String,
        likedUsers: (ArrayList<String>) -> Unit
    ) {
        mFireStore.collection(Constants.ANNOUNCEMENTS)
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
                    likedUsers(document!!.likedBy)

                }

            }
    }


    fun addLike(likedUser: String, documentId: String) {
        mFireStore.collection(Constants.ANNOUNCEMENTS)
            .document(documentId)
            .update("likedBy", FieldValue.arrayUnion(likedUser))
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.e(TAG, "postAnnouncement: Error while posting announcement")
            }
    }

    fun updateLikeHasMap(updateHashMap: HashMap<String, Any>, documentId: String) {
        mFireStore.collection(Constants.ANNOUNCEMENTS)
            .document(documentId)
            .update(updateHashMap)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.e(TAG, "postAnnouncement: Error while posting announcement")
            }
    }

    fun removeLike(likedBy: String, documentId: String) {
        mFireStore.collection(Constants.ANNOUNCEMENTS)
            .document(documentId)
            .update("likedBy", FieldValue.arrayRemove(likedBy))
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.e(TAG, "postAnnouncement: Error while posting announcement")
            }
    }


    fun getUpdatedLikes(announcementId: String, announcement: (likes: Long) -> Unit) {
        mFireStore.collection(Constants.ANNOUNCEMENTS)
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
                    announcement(document!!.likeCount)

                }

            }
    }
}