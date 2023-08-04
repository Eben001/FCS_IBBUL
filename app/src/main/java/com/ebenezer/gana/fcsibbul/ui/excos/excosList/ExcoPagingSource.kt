package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.data.repository.excos.ExcosRepository
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class ExcoPagingSource(private val repository: ExcosRepository) :
    PagingSource<QuerySnapshot, Exco>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Exco> {
        return try {
            val currentPage = params.key ?: repository.getExcos().get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = repository.getExcos().startAfter(lastVisibleProduct).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(Exco::class.java),
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
