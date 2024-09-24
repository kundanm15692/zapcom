package com.example.zapcom.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.zapcom.data.model.ItemList
import com.example.zapcom.data.repository.MainActivityRepository
import com.example.zapcom.utils.UiState
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainActivityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ItemList>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<ItemList>>> = _uiState


    @SuppressLint("SuspiciousIndentation")
    fun getItemList() {

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                  val result = mainRepository.getUsers()
                    result.collect { list ->
                        println("Received list: $list")
                        _uiState.value = UiState.Success(list)
                    }
            }catch (e:Exception){
                 _uiState.value = UiState.Error(e.toString())
            }
        }

    }


}