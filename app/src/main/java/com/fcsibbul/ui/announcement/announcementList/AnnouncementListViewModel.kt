package com.fcsibbul.ui.announcement.announcementList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.fcsibbul.data.repository.announcement.AnnouncementRepository
import com.fcsibbul.utils.Constants.PAGE_SIZE

class AnnouncementListViewModel constructor(private val repository: AnnouncementRepository) :
    ViewModel() {


    val announcementPagingFlow = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE)
    ){
        AnnouncementPagingSource(repository)
    }.flow.cachedIn(viewModelScope)


}