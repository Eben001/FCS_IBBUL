package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ebenezer.gana.fcsibbul.R
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.data.repository.excos.ExcosRepository
import com.ebenezer.gana.fcsibbul.utils.Constants.PAGE_SIZE
import com.ebenezer.gana.fcsibbul.utils.UiText

class ExcosViewModel constructor(private val repository: ExcosRepository) : ViewModel() {

    private val _excos = MutableLiveData<List<Exco>>()
    val excos: LiveData<List<Exco>> = _excos
    private var _isDeleteSuccess = MutableLiveData<Boolean>()
    val isDeleteSuccess: LiveData<Boolean> = _isDeleteSuccess

    private var _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result

    private var _isAdmin = MutableLiveData<Boolean>()
    val isAdmin: LiveData<Boolean> = _isAdmin


    fun verifyIfAdmin() {
        repository.verifyIfAdmin {
            when (it.role) {
                1 -> {
                    _isAdmin.value = true
                }

                0 -> {
                    _isAdmin.value = false


                }
            }

        }
    }

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
                _result.value = UiText.StringResource(R.string.success_delete)

            },
            onFailure = {
                _isDeleteSuccess.value = false
                _result.value = UiText.DynamicString(it)

            })
    }

}