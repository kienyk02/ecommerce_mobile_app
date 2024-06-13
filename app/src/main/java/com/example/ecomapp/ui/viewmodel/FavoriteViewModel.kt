package com.example.ecomapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.data.Favorite
import com.example.ecomapp.repositories.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private val favoriteRepository = FavoriteRepository()

    private val _favorites = MutableLiveData<List<Favorite>>()
    private val _favorite = MutableLiveData<Favorite>()

    val favorites : MutableLiveData<List<Favorite>>
        get() = _favorites

    val  favorite : MutableLiveData<Favorite>
        get() = _favorite

    fun fetchFavoriteByUserId(userId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = favoriteRepository.fetchFavoriteByUserId(userId)
            if (response.isSuccessful) {
                favorites.postValue(response.body())
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.errorBody()}"
                )
            }
        }
    }

    fun saveFavorite(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = favoriteRepository.saveFavorite(favorite)
            if (response.isSuccessful) {
                Log.i("FAVORITE", "POST: ${response.body().toString()}")
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.errorBody()}"
                )
            }
        }
    }

    fun deleteFavorite(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = favoriteRepository.deleteFavorite(id)
            if (response.isSuccessful) {
                Log.i("FAVORITE", "DELETE: ${response.body().toString()}")
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.errorBody()}"
                )
            }
        }
    }

    fun checkIfProductFavorite(userId: Int, productId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = favoriteRepository.checkIfProductFavorite(userId, productId)
            if (response.isSuccessful) {
                favorite.postValue(response.body())
            }
            else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.errorBody()}"
                )
            }
        }
    }

}