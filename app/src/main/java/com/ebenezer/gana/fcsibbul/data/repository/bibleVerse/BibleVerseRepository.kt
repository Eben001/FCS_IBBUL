package com.ebenezer.gana.fcsibbul.data.repository.bibleVerse

import android.util.Log
import com.ebenezer.gana.fcsibbul.data.models.DailyBibleVerse
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

private const val TAG = "BibleVerseRepository"

class BibleVerseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun getDailyBibleVerse(bibleVerse: (DailyBibleVerse) -> Unit) {
        firestore.collection(Constants.BIBLE_VERSE)
            .document(Constants.BIBLE_VERSE)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d(TAG, "getBibleVerse: Listen Failed", error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val document = value.toObject(DailyBibleVerse::class.java)
                    bibleVerse(document!!)


                }
            }
    }


}