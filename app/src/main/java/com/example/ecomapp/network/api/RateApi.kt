package com.example.ecomapp.network.api

import com.example.ecomapp.data.Rate
import retrofit2.http.Body
import retrofit2.http.POST

interface RateApi {
    @POST("api/insert-rate")
    suspend fun insertRate(@Body rate: Rate): Rate
}