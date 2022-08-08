package com.ebenezer.gana.fcsibbul.ui.announcement.announcementList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementListViewModel @Inject constructor(private val repository: AnnouncementRepository) :
    ViewModel() {

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