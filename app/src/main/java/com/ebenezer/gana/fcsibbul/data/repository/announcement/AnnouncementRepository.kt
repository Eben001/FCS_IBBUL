package com.ebenezer.gana.fcsibbul.data.repository.announcement

import android.util.Log
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.data.models.User
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

private const val TAG = "AnnouncementRepository"

class AnnouncementRepository(private val firestore: FirebaseFirestore) {

    /**
     * Gets announcement list from firestore
     */
    fun getAnnouncements(result: (MutableList<Announcement>) -> Unit) {
        firestore.collection(Constants.ANNOUNCEMENTS)
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { document ->
                val announcementsList = mutableListOf<Announcement>()
                val documents = document.documents

                documents.forEach {
                    // we want to get the id of each document
                    val announcement = it.toObject(Announcement::class.java)
                    if (announcement != null) {
                        announcement.announcementId = it.id
                        announcementsList.add(announcement)
                    }
                }
                result(announcementsList)
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting announcement: ${it.printStackTrace()}")

            }

    }


    /**
     * Posts new announcement
     */
    fun postAnnouncement(
        newAnnouncement: Announcement,
        onSuccess: (UiText) -> Unit,
        onFailure: (UiText) -> Unit
    ) {

        //Gets the id of the user who posted the announcement and go ahead with posting
        val announcement = Announcement(
            userId = getCurrentUserId(),
            title = newAnnouncement.title,
            details = newAnnouncement.details,
            date = newAnnouncement.date,
            likeCount = newAnnouncement.likeCount
        )

        firestore.collection(Constants.ANNOUNCEMENTS)
            .document()
            .set(announcement, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess(UiText.StringResource(R.string.posted_success))
            }
            .addOnFailureListener {
                onFailure(UiText.DynamicString(it.message!!))
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