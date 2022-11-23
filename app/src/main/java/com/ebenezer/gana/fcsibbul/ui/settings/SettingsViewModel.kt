package com.ebenezer.gana.fcsibbul.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository


class SettingsViewModel constructor(private val repository: FcsRepository) : ViewModel() {

    val loggedOut: LiveData<Boolean> = repository.loggedOut
    fun logoutUser() {
        repository.logoutUser()
    }

}