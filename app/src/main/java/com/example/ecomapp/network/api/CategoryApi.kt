package com.example.ecomapp.network.api

import com.example.ecomapp.data.Category
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {

    @GET("/categories")
    suspend fun getCategories(): Response<List<Category>>

}