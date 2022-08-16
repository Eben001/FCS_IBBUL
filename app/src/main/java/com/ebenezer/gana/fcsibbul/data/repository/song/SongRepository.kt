package com.ebenezer.gana.fcsibbul.data.repository.song

import android.util.Log
import com.ebenezer.gana.fcsibbul.data.models.Song
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

private const val TAG = "SongRepository"

class SongRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    fun getSong(song: (String) -> Unit) {
        firestore.collection(Constants.SONG)
            .document(Constants.SONG)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d(TAG, "getSong: Listen Failed", error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val document = value.toObject(Song::class.java)
                    if (document != null) {
                        song(document.content)
                    }

                }
            }

    }
}