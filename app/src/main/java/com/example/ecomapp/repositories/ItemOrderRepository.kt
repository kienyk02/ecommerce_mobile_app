package com.example.ecomapp.repositories

import android.app.Application
import com.example.ecomapp.database.AppDatabase
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.ItemOrderApi

class ItemOrderRepository(app: Application) {
    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(app)
    }

    private val itemOrderApi: ItemOrderApi = ApiConfig.retrofit.create(ItemOrderApi::class.java)

    suspend fun getItemOrderByRated() = itemOrderApi.getItemOrderByRated()

    suspend fun updateItemOrderByRated(id: Int, stars:Int) = itemOrderApi.updateItemOrderByRated(id, stars)
}