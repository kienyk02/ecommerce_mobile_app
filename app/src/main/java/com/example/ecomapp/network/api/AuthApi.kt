package com.example.ecomapp.network.api

import com.example.ecomapp.auth.MessageResponse
import com.example.ecomapp.auth.SigninRequest
import com.example.ecomapp.auth.SignupRequest
import com.example.ecomapp.auth.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/auth/signup")
    suspend fun signUp(@Body request: SignupRequest): MessageResponse

    @POST("api/v1/auth/signin")
    suspend fun signIn(@Body loginRequest: SigninRequest): TokenResponse

}