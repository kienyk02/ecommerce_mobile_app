package com.example.ecomapp.network.api

import com.example.ecomapp.data.Search
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface SearchApi {

    @GET("/search/{userId}")
    suspend fun getSearchByUserId(@Path(value = "userId") id : Int): Response<List<Search>>

    @POST("/search")
    suspend fun saveSearch(@Body search: Search): Response<Search>

    @DELETE("/search/{id}")
    suspend fun deleteSearch(@Path(value = "id") id: Int): Response<Search>

}