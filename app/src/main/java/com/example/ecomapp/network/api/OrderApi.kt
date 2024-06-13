package com.example.ecomapp.network.api

import com.example.ecomapp.data.Order
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApi {
    @GET("api/order-all")
    suspend fun getAllOrder(): List<Order>

    @GET("api/orders")
    suspend fun getOrdersByStatus(@Query("status") status: String): List<Order>

    @GET("api/orders/search")
    suspend fun searchingOrder(@Query("key") key: String): List<Order>

    @GET("api/order/get/{id}")
    suspend fun getOrderById(@Path("id") id: Int): Order

    @POST("api/create-order")
    suspend fun createOrder(@Body order: Order): Order

    @POST("api/order/update/{id}")
    suspend fun updateOrderStatus(@Path("id") id: Int, @Body status: String)

    @DELETE("api/order/delete/{id}")
    suspend fun cancelOrder(@Path("id") id: Int)
}