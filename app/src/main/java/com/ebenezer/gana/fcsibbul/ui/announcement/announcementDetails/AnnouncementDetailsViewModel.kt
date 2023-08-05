package com.ebenezer.gana.fcsibbul.ui.announcement.announcementDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementDetailsRepository
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class AnnouncementDetailsViewModel constructor(private val repository: AnnouncementDetailsRepository) :
    ViewModel() {

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

    fun checkDocumentExists(documentId: String, onDocumentExists: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = repository.isDocumentExists(documentId)
            onDocumentExists(exists)
        }
    }

    fun checkLike(likedBy: ArrayList<String>) {
        _alreadyLiked.value = likedBy.contains(getCurrentUserId())
    }

    fun deleteAnnouncement(announcementId: String) {
        repository.deleteAnnouncement(announcementId,
            onSuccess = { _isDeleteSuccess.value = true },
            onFailure = { _isDeleteSuccess.value = false })
    }

    fun likeOrUnlikeAnnouncement(totalLikes: Long, likedBy: ArrayList<String>, documentId: String) {
        val alreadyLiked = likedBy.contains(getCurrentUserId())

        if (alreadyLiked) {
            repository.removeLike(getCurrentUserId(), documentId)
            _likesCount.value = totalLikes - 1
        } else {
            repository.addLike(getCurrentUserId(), documentId)
            _likesCount.value = totalLikes + 1
        }

        _alreadyLiked.value = !alreadyLiked

        val updateHashMap = hashMapOf("likeCount" to _likesCount.value!!)
        repository.updateLikeMap(updateHashMap, documentId)
    }

    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
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