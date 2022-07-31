package com.ebenezer.gana.fcsibbul.ui.announcement.announcementDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementDetailsRepository
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

    var likeChecker = false

    fun checkLike(likedBy: ArrayList<String>) {
        _alreadyLiked.value = likedBy.contains(getCurrentUserId())
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