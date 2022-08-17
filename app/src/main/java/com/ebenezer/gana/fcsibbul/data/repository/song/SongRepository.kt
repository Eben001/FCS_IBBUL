package com.ebenezer.gana.fcsibbul.data.repository.song

import android.util.Log
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Song
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

private const val TAG = "SongRepository"

class SongRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun getSong(song: (Song) -> Unit) {
        firestore.collection(Constants.SONG)
            .document(Constants.SONG)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d(TAG, "getSong: Listen Failed", error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val document = value.toObject(Song::class.java)
                    song(document!!)


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