package com.ebenezer.gana.fcsibbul.ui.announcement.announcementDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementDetailsRepository
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth

class AnnouncementDetailsViewModel : ViewModel() {

    private val repository = AnnouncementDetailsRepository()

    private val _likesCount = MutableLiveData<Long>()
    val likesCount: LiveData<Long> = _likesCount

    private val _updatedLikeCount = MutableLiveData<Long?>()
    val updatedLikeCount: LiveData<Long?> = _updatedLikeCount

    private val _updatedLikedUsers = MutableLiveData<ArrayList<String>>()
    val updatedLikedUsers: LiveData<ArrayList<String>> = _updatedLikedUsers


    private val _alreadyLiked = MutableLiveData<Boolean>()
    val alreadyLiked: LiveData<Boolean> = _alreadyLiked

    private var _isDeleteSuccess = MutableLiveData<Boolean>()
    val isDeleteSuccess: LiveData<Boolean> = _isDeleteSuccess

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


    var likeChecker = false

    fun checkLike(likedBy: ArrayList<String>) {
        _alreadyLiked.value = likedBy.contains(getCurrentUserId())
    }

    fun deleteAnnouncement(announcementId:String) {
        repository.deleteAnnouncement(announcementId,
            onSuccess = {
                _isDeleteSuccess.value = true
                _result.value = it
            }, onFailure = {
                _isDeleteSuccess.value = false
                _result.value = it
            })
    }

    fun likeAnnouncement(totalLikes: Long, likedBy: ArrayList<String>, documentId: String) {
        val updateHashMap = HashMap<String, Any>()

        likeChecker = true
        if (likeChecker) {
            if (likedBy.contains(getCurrentUserId())) {
                if (totalLikes > 0) {
                    repository.removeLike(getCurrentUserId(), documentId)
                    likeChecker = false
                    _alreadyLiked.value = false

                    val total = totalLikes - 1
                    _likesCount.value = total
                    updateHashMap["likeCount"] = total
                    repository.updateLikeHasMap(updateHashMap, documentId)
                }

            } else {
                repository.addLike(getCurrentUserId(), documentId)
                likeChecker = false
                _alreadyLiked.value = true

                val total = totalLikes + 1
                _likesCount.value = total
                updateHashMap["likeCount"] = total
                repository.updateLikeHasMap(updateHashMap, documentId)

            }
        }


    }


    private fun getCurrentUserId(): String {
        var currentUserID = ""

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            currentUserID = it.uid
        }
        return currentUserID
    }


    fun getUpdatedLikedUsers(announcementId: String) {
        repository.getUpdatedLikedUsers(announcementId) { likedUsers ->
            _updatedLikedUsers.value = likedUsers
        }
    }

    fun getUpdatedLikes(announcementId: String) {
        repository.getUpdatedLikes(announcementId) { likesCount ->
            _updatedLikeCount.value = likesCount
        }
    }
}