package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import android.app.Application
import androidx.lifecycle.*
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.data.repository.excos.ExcosRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExcosViewModel @Inject constructor(private val repository: ExcosRepository) : ViewModel() {


    private val _excos = MutableLiveData<MutableList<Exco>>()
    val excos: LiveData<MutableList<Exco>> = _excos


    fun getExcosList() {
        viewModelScope.launch {
            repository.getExcos {
                _excos.value = it
            }
        }
    }

}