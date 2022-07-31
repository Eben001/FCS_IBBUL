package com.ebenezer.gana.fcsibbul.ui.admin.postAnnouncement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementRepository
import com.ebenezer.gana.fcsibbul.utils.UiText

class PostAnnouncementViewModel : ViewModel() {
    private val repository = AnnouncementRepository()

    private var _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result

    private var _isAdmin = MutableLiveData<Boolean>()
    val isAdmin: LiveData<Boolean> = _isAdmin

    /**
     * Function to verify the current logged in user's role if the user is admin or normal user
     */
    fun verifyIfAdmin() {
        repository.verifyIfAdmin {
            when (it.role) {
                0 -> {
                    _isAdmin.value = false

                }
                1 -> {
                    _isAdmin.value = true
                }

                // 0 - Normal user, 1 - Admin

            }

        }
    }

    private fun postNewAnnouncement(newAnnouncement: Announcement) {
        repository.postAnnouncement(newAnnouncement)
    }

    /**
     * Function to be called from PostAnnouncementFragment to prepare the announcement details to be posted
     * @param id the id of the current logged in user
     * @param title the title of the announcement
     * @param details announcement details
     * @param date date and time the announcement was made.
     */
    fun postAnnouncement(
        userId: String,
        announcementId: String,
        title: String,
        details: String,
        date: String,
        likeCount: Long

    ) {
        val newData =
            Announcement(userId, announcementId, title, details, date, likeCount = likeCount)
        postNewAnnouncement(newData)
    }

}