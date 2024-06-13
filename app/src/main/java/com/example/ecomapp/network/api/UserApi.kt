package com.example.ecomapp.network.api

import com.example.ecomapp.auth.MessageResponse
import com.example.ecomapp.data.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserApi {

    @GET("api/user-info")
    suspend fun getUserInfo(): User

    @Multipart
    @PUT("api/user/update-info")
    suspend fun updateUserInfo(
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part avatarImage: MultipartBody.Part
    ): MessageResponse

    @Multipart
    @PUT("api/address/update")
    suspend fun updateAddress(
        @Part("address") address: RequestBody
    ): MessageResponse

}