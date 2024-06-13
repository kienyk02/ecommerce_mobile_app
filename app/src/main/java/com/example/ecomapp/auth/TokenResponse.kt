package com.example.ecomapp.auth

data class TokenResponse(
    val token: String,
    val userId: Int,
    val userName: String,
    val email: String,
    val roles: List<String>
)
