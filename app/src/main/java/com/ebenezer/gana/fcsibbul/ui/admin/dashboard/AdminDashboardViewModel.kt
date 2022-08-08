package com.ebenezer.gana.fcsibbul.ui.admin.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.repository.FcsRepository
import com.ebenezer.gana.fcsibbul.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(private val repository: FcsRepository) : ViewModel() {

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
                    _appBarTitle.value = UiText.StringResource(R.string.error)
                    _isAdmin.value = false


                }
            }

        }
    }

}