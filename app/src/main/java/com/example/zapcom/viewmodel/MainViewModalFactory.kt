package com.example.zapcom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zapcom.data.repository.MainActivityRepository

class MainViewModelFactory(private val repository: MainActivityRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }


    }
}