package com.ebenezer.gana.fcsibbul.data.repository.bibleVerse

import android.util.Log
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.DailyBibleVerse
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.ebenezer.gana.fcsibbul.utils.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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

    /**
     * Posts a new bible verse
     */
    fun postDailyBibleVerse(
        newDailyBibleVerse: DailyBibleVerse,
        onSuccess: (UiText) -> Unit, onFailure: (UiText) -> Unit
    ) {
        var currentUserId = ""
        firebaseAuth.currentUser?.let {
            currentUserId = it.uid
        }
        val dailyBibleVerse = DailyBibleVerse(
            posterId = currentUserId,
            content = newDailyBibleVerse.content
        )
        firestore.collection(Constants.BIBLE_VERSE)
            .document(Constants.BIBLE_VERSE)
            .set(dailyBibleVerse, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess(UiText.StringResource(R.string.success))
            }
            .addOnFailureListener {
                onFailure(UiText.DynamicString(it.message!!))
            }
    }


}