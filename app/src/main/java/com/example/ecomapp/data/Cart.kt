package com.example.ecomapp.data


data class Cart(
    val id: Int? = null,
    val product: Product,
    var quantity: Int
)