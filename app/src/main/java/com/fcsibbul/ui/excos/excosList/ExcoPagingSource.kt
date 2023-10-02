package com.fcsibbul.ui.excos.excosList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fcsibbul.data.models.Exco
import com.fcsibbul.data.repository.excos.ExcosRepository
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await


private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLIS = 3000L // 3 second
class ExcoPagingSource(private val repository: ExcosRepository) :
    PagingSource<QuerySnapshot, Exco>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Exco> {
        return try {
            val startKey = params.key ?: STARTING_KEY
            // Simulate a delay for loads after the initial load
            if (startKey != STARTING_KEY) {
                delay(LOAD_DELAY_MILLIS)
            }


            val currentPage = params.key ?: repository.getExcos().get().await()
            val lastVisibleItem = currentPage.documents[currentPage.size() - 1]
            val nextPage = repository.getExcos().startAfter(lastVisibleItem).get().await()
            val excosList = mutableListOf<Exco>()

            for(item in currentPage){
                val excoItem = item.toObject(Exco::class.java)
                excoItem.documentId = item.id
                excosList.add(excoItem)
            }

            LoadResult.Page(
                data = excosList,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Exco>): QuerySnapshot? {
        return null
    }
}
