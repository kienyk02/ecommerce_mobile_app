package com.example.ecomapp.repositories

import android.app.Application
import com.example.ecomapp.database.AppDatabase
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.UserApi
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository(app: Application): BaseRepository() {

    init {
        val appDatabase: AppDatabase = AppDatabase.getInstance(app)
    }

    private val userApi: UserApi = ApiConfig.retrofit.create(UserApi::class.java)

    suspend fun getUserInfo() = safeApiCall {
        userApi.getUserInfo()
    }

    suspend fun updateUserInfo(name: RequestBody, gender: RequestBody, phoneNumber: RequestBody, avatarImage: MultipartBody.Part)
        = userApi.updateUserInfo(name, gender, phoneNumber, avatarImage)

    suspend fun updateAddress(address: RequestBody) = userApi.updateAddress(address)

}