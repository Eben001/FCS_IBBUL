package com.fcsibbul.ui.announcement.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _announcementDeleted = MutableLiveData<Boolean>()
    val announcementDeleted: LiveData<Boolean>
        get() = _announcementDeleted

    fun setAnnouncementDeleted(value: Boolean) {
        _announcementDeleted.value = value
    }
}
