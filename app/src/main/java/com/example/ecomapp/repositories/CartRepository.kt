package com.example.ecomapp.repositories

import android.app.Application
import com.example.ecomapp.data.Cart
import com.example.ecomapp.database.AppDatabase
import com.example.ecomapp.network.ApiConfig
import com.example.shoppingapp.api.CartApi


class CartRepository(app: Application) {
    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(app)
    }

    private val cartApi: CartApi = ApiConfig.retrofit.create(CartApi::class.java)

    suspend fun getAllCart() = cartApi.getAllCart()

    suspend fun addCart(cart: Cart) = cartApi.addCart(cart)

    suspend fun updateCartQuantity(id: Int, quantity: Int) = cartApi.updateCartQuantity(id, quantity)

    suspend fun deleteCartById(id: Int) = cartApi.deleteCartById(id)

    suspend fun deleteCartByUser() = cartApi.deleteCartByUser()

    suspend fun getModels() = cartApi.getModels()

    suspend fun getTest() = cartApi.getTest()
}