package com.example.ecomapp.auth

data class SignupRequest(
    val name: String,
    val gender: String,
    val email: String,
    val password: String
)
