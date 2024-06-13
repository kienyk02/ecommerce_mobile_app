package com.example.ecomapp.repositories

import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.ProductSuggestCFApi

class SuggestProductRepository {
    private val productSuggestCFApi: ProductSuggestCFApi =
        ApiConfig.retrofitAICFServer.create(ProductSuggestCFApi::class.java)

    suspend fun getProductsSuggestWithCF(userId: Int, quantity: Int) =
        productSuggestCFApi.getProductsSuggestWithCF(userId, quantity)
}