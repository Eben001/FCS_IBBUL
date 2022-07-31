package com.ebenezer.gana.fcsibbul.ui.announcement.announcementList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementRepository
import kotlinx.coroutines.launch

class AnnouncementListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AnnouncementRepository = AnnouncementRepository()

    private val _announcements = MutableLiveData<MutableList<Announcement>>()
    val announcement: LiveData<MutableList<Announcement>> = _announcements


    fun getAnnouncements() {
        viewModelScope.launch {
            repository.getAnnouncements {
                _announcements.value = it
            }
        }
    }


}