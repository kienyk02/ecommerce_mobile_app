package com.example.ecomapp.network.api

import com.example.ecomapp.data.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductSuggestCFApi {
    @GET("get-recommend-bookids/{userId}/{quantity}")
    suspend fun getProductsSuggestWithCF(
        @Path("userId") userId: Int,
        @Path("quantity") quantity: Int): List<Product>
}