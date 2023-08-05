package com.ebenezer.gana.fcsibbul.ui.announcement.announcementList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ebenezer.gana.fcsibbul.data.models.Announcement
import com.ebenezer.gana.fcsibbul.data.repository.announcement.AnnouncementRepository
import com.ebenezer.gana.fcsibbul.utils.Constants.PAGE_SIZE
import kotlinx.coroutines.launch

class AnnouncementListViewModel constructor(private val repository: AnnouncementRepository) :
    ViewModel() {

//    private val _announcements = MutableLiveData<MutableList<Announcement>>()
//    val announcement: LiveData<MutableList<Announcement>> = _announcements
//

    val announcementPagingFlow = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE)
    ){
        AnnouncementPagingSource(repository)
    }.flow.cachedIn(viewModelScope)


//    fun getAnnouncements() {
//        viewModelScope.launch {
//            repository.getAnnouncements {
//                _announcements.value = it
//            }
//        }
//    }


}