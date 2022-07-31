package com.ebenezer.gana.fcsibbul.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FcsRepository = FcsRepository()

    val loggedOut:LiveData<Boolean>  = repository.loggedOut
    fun logoutUser(){
        repository.logoutUser()
    }

}