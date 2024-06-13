package com.example.ecomapp.repositories

import com.example.ecomapp.data.Favorite
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.FavoriteApi
import retrofit2.Response

class FavoriteRepository {

    private val favoriteService = ApiConfig.retrofit.create(FavoriteApi::class.java)

    suspend fun fetchFavoriteByUserId(userId : Int) : Response<List<Favorite>> {
        return favoriteService.getFavoriteByUserId(userId)
    }

    suspend fun saveFavorite(favorite: Favorite) : Response<Favorite> {
        return favoriteService.saveFavorite(favorite)
    }

    suspend fun deleteFavorite(id: Int): Response<Favorite> {
        return favoriteService.deleteFavorite(id)
    }

    suspend fun checkIfProductFavorite(userId: Int, productId : Int) : Response<Favorite> {
        return favoriteService.checkIfProductFavorite(userId, productId);
    }

}