package com.ebenezer.gana.fcsibbul.ui.admin.addExco

import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.models.Exco


class AddExcoViewModel : ViewModel() {


    fun addExco(
        id:String,
        firstName: String,
        lastName: String,
        emailId: String,
        imageUrl: String,
        phoneNumber: String,
        office: String
    ) {
        val newData =
            Exco(id, firstName, lastName, emailId,
            imageUrl, phoneNumber, office)
    }


}