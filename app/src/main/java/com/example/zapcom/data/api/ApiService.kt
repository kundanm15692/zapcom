package com.example.zapcom.data.api

import com.example.zapcom.data.model.ItemList
import com.example.zapcom.data.model.User
import retrofit2.http.GET

interface ApiService {

    @GET("/b/5BEJ")
    suspend fun getUsers(): List<ItemList>
}