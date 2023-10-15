package com.fcsibbul.data.repository.song

import com.fcsibbul.R
import com.fcsibbul.data.models.Song
import com.fcsibbul.utils.Constants
import com.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import timber.log.Timber


class SongRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun getSong(song: (Song) -> Unit) {
        firestore.collection(Constants.SONG)
            .document(Constants.SONG)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Timber.d( "getSong: Listen Failed", error)
                    return@addSnapshotListener
                }
                if (value != null) {
                   value.toObject(Song::class.java)
                        ?.let {document->
                            song(document)
                        }


                }
            }

    }

    /**
     * Posts a new song
     */
    fun postSong(newSong: Song, onSuccess: (UiText) -> Unit, onFailure: (UiText) -> Unit) {
        val song = Song(
            posterId = getCurrentUserId(),
            newSong.title,
            newSong.content
        )
        firestore.collection(Constants.SONG)
            .document(Constants.SONG)
            .set(song, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess(UiText.StringResource(R.string.success))

            }
            .addOnFailureListener {
                onFailure(UiText.DynamicString(it.message!!))

            }
    }

    /**
     * Gets the current logged in user id
     */
    private fun getCurrentUserId(): String {
        var currentUserID = ""

        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            currentUserID = it.uid
        }
        return currentUserID
    }
}