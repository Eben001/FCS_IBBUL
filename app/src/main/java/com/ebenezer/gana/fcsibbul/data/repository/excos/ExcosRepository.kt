package com.ebenezer.gana.fcsibbul.data.repository.excos

import android.util.Log
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "ExcosRepository"

class ExcosRepository {
    private var mFireStore = FirebaseFirestore.getInstance()

    fun getExcos(excos: (MutableList<Exco>) -> Unit) {
        mFireStore.collection(Constants.EXCOS)
            .get()
            .addOnSuccessListener { document ->
                val excosList = mutableListOf<Exco>()

                for (item in document) {
                    val list = item.toObject(Exco::class.java)
                    excosList.add(list)
                }
                excos(excosList)
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting excos list ${it.printStackTrace()}")
            }
    }
}