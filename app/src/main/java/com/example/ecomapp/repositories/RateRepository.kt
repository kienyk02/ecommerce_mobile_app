package com.example.ecomapp.repositories

import android.app.Application
import com.example.ecomapp.data.Rate
import com.example.ecomapp.database.AppDatabase
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.RateApi

class RateRepository(app: Application) {
    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(app)
    }

    private val rateApi: RateApi = ApiConfig.retrofit.create(RateApi::class.java)

    suspend fun insertRate(rate: Rate) = rateApi.insertRate(rate)
}