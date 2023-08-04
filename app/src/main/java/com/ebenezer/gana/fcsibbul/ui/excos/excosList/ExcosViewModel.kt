package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.data.repository.excos.ExcosRepository
import com.ebenezer.gana.fcsibbul.utils.Constants.PAGE_SIZE
import com.firebase.ui.firestore.paging.FirestorePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExcosViewModel constructor(private val repository: ExcosRepository) : ViewModel() {

    private val _excos = MutableLiveData<List<Exco>>()
    val excos: LiveData<List<Exco>> = _excos
    private var _isDeleteSuccess = MutableLiveData<Boolean>()
    val isDeleteSuccess: LiveData<Boolean> = _isDeleteSuccess


    val excoPagingFlow = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),

        ) {
        ExcoPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun getExcosList() {
        /*        viewModelScope.launch {
                    repository.getExcos {
                        _excos.value = it
                    }
                }*/
    }

    fun deleteExcoDetails(excoDocumentID: String, imageUrl: String) {
        repository.deleteExcoDetails(excoDocumentID, imageUrl,
            onSuccess = {
                _isDeleteSuccess.value = true

            },
            onFailure = {
                _isDeleteSuccess.value = false

            })
    }

}