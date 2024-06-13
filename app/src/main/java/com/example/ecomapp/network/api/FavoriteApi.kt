package com.example.ecomapp.network.api

import com.example.ecomapp.data.Favorite
import com.example.ecomapp.data.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteApi {

    @GET("/favorite/{userId}")
    suspend fun getFavoriteByUserId(@Path("userId") id : Int): Response<List<Favorite>>

    @POST("/favorite")
    suspend fun saveFavorite(@Body favorite: Favorite) : Response<Favorite>

    @DELETE("/favorite/{id}")
    suspend fun deleteFavorite(@Path("id") id: Int): Response<Favorite>

    @GET("/favorite/user/{userId}/product/{productId}")
    suspend fun checkIfProductFavorite(@Path("userId") userId : Int, @Path("productId") productId : Int) : Response<Favorite>


}