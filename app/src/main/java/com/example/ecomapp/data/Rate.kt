package com.example.ecomapp.data

data class Rate(
    val id: Int? = null,
    val product: Product,
    val user: User?,
    val stars: Int
)