package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.data.repository.excos.ExcosRepository
import kotlinx.coroutines.launch

class ExcosViewModel constructor(private val repository: ExcosRepository) : ViewModel() {

    private val _excos = MutableLiveData<List<Exco>>()
    val excos: LiveData<List<Exco>> = _excos


    fun getExcosList() {
        viewModelScope.launch {
            repository.getExcos {
                _excos.value = it
            }
        }
    }

}