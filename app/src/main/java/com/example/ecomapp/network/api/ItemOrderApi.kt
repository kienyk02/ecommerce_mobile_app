package com.example.ecomapp.network.api

import com.example.ecomapp.data.ItemOrder
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ItemOrderApi {
    @GET("api/item-order-rated")
    suspend fun getItemOrderByRated(): List<ItemOrder>

    @POST("api/item-order-rated/{id}/{stars}")
    suspend fun updateItemOrderByRated(@Path("id") id: Int,@Path("stars") stars:Int)

}