package com.ebenezer.gana.fcsibbul.ui.excos.excosList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.fcsibbul.data.models.Exco
import com.ebenezer.gana.fcsibbul.data.repository.excos.ExcosRepository
import kotlinx.coroutines.launch

class ExcosViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExcosRepository = ExcosRepository()

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