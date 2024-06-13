package com.example.ecomapp.data

data class User(
    val id: Int,
    val name: String? = null,
    val phoneNumber: String? = null,
    val gender: String? = null,
    val avatarImage: String? = null,
    val address: Address? = null
)