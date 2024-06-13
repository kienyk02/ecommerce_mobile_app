package com.example.ecomapp.repositories

import android.app.Application
import com.example.ecomapp.data.Order
import com.example.ecomapp.database.AppDatabase
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.OrderApi

class OrderRepository(app: Application) {
    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(app)
    }

    private val orderApi: OrderApi = ApiConfig.retrofit.create(OrderApi::class.java)

    suspend fun getAllOrder() = orderApi.getAllOrder()

    suspend fun getOrdersByStatus(status: String) = orderApi.getOrdersByStatus(status)

    suspend fun searchingOrder(key: String) = orderApi.searchingOrder(key)

    suspend fun getOrderById(id: Int) = orderApi.getOrderById(id)

    suspend fun createOrder(order: Order) = orderApi.createOrder(order)

    suspend fun updateOrderStatus(id: Int, status: String) = orderApi.updateOrderStatus(id, status)

    suspend fun cancelOrder(id: Int) = orderApi.cancelOrder(id)

}