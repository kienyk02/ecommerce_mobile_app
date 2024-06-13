package com.example.ecomapp.data

data class Payment(
    val id: Int? = null,
    val totalPrice: Int,
    val payStatus: String,
    val paymentMethod: PaymentMethod
)