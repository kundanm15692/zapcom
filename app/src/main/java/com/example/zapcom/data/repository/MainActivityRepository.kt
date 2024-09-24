package com.example.zapcom.data.repository

import com.example.zapcom.data.api.ApiService
import com.example.zapcom.data.model.ItemList
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MainActivityRepository @Inject constructor(private val apiService: ApiService) {

    fun getUsers(): Flow<List<ItemList>> {
        return flow {
            emit(apiService.getUsers())
        }.map {
            it
        }
    }
}