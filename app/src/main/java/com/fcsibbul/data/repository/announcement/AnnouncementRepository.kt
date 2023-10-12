package com.fcsibbul.data.repository.announcement

import com.fcsibbul.R
import com.fcsibbul.data.models.Announcement
import com.fcsibbul.data.models.User
import com.fcsibbul.utils.Constants
import com.fcsibbul.utils.Constants.PAGE_SIZE
import com.fcsibbul.utils.UiText
import com.google.errorprone.annotations.Keep
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import timber.log.Timber

@Keep
class AnnouncementRepository(private val firestore: FirebaseFirestore) {

    /**
     * Gets announcement list from firestore
     */
    fun getAnnouncements()  =
        firestore.collection(Constants.ANNOUNCEMENTS)
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .limit(PAGE_SIZE.toLong())


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