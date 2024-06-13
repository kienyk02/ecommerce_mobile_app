package com.example.ecomapp.network.api

import com.example.ecomapp.data.PaymentMethod
import retrofit2.http.GET

interface PaymentMethodApi {
    @GET("api/paymentmethods")
    suspend fun getPaymentMethods(): List<PaymentMethod>
}