package com.fcsibbul.ui.announcement.announcementList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fcsibbul.data.models.Announcement
import com.fcsibbul.data.repository.announcement.AnnouncementRepository
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLIS = 2000L
class AnnouncementPagingSource(private val repository: AnnouncementRepository) :
    PagingSource<QuerySnapshot, Announcement>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Announcement> {
        return try {
            val startKey = params.key ?: STARTING_KEY
            // Simulate a delay for loads after the initial load
            if (startKey != STARTING_KEY) {
                delay(LOAD_DELAY_MILLIS)
            }


            val currentPage = params.key ?: repository.getAnnouncements().get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = repository.getAnnouncements().startAfter(lastVisibleProduct).get().await()

            val announcementsList = mutableListOf<Announcement>()
            for (document in currentPage) {
                val announcement = document.toObject(Announcement::class.java)
                announcement.announcementId = document.id
                announcementsList.add(announcement)
            }

            LoadResult.Page(
                data = announcementsList,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Announcement>): QuerySnapshot? {
        return null
    }
}