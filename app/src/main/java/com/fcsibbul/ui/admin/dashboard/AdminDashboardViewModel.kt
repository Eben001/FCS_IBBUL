package com.fcsibbul.ui.admin.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fcsibbul.R
import com.fcsibbul.data.repository.FcsRepository
import com.fcsibbul.utils.UiText

class AdminDashboardViewModel (private val repository: FcsRepository) : ViewModel() {

    private var _isAdmin = MutableLiveData<Boolean>()
    val isAdmin: LiveData<Boolean> = _isAdmin

    private var _appBarTitle = MutableLiveData<UiText>()
    val appBarTitle: LiveData<UiText> = _appBarTitle

    fun verifyIfAdmin() {
        repository.verifyIfAdmin {
            when (it.role) {
                1 -> {
                    _appBarTitle.value = UiText.StringResource(R.string.dashboard)
                    _isAdmin.value = true
                }

                0 -> {
                    _appBarTitle.value = UiText.StringResource(R.string.not_authorized)
                    _isAdmin.value = false


                }
            }

        }
    }

}