package com.example.ecomapp.repositories

import android.app.Application
import com.example.ecomapp.auth.SigninRequest
import com.example.ecomapp.auth.SignupRequest
import com.example.ecomapp.database.AppDatabase
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.JwtManager
import com.example.ecomapp.network.api.AuthApi

class AuthRepository(app: Application): BaseRepository() {

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(app)
    }

    private val authApi: AuthApi = ApiConfig.retrofitWithoutJwt.create(AuthApi::class.java)

    suspend fun signIn(request: SigninRequest) = safeApiCall {
        authApi.signIn(request)
    }

    suspend fun signUp(request: SignupRequest) = safeApiCall {
        authApi.signUp(request)
    }

}