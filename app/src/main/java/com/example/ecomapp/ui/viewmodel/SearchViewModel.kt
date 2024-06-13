package com.example.ecomapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.data.Search
import com.example.ecomapp.repositories.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val searchRepository = SearchRepository()

    private val _searches = MutableLiveData<List<Search>>()

    val searches: MutableLiveData<List<Search>>
        get() = _searches

    fun fetchSearchByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository.fetchSearchByUserId(userId)
            if (response.isSuccessful) {
                searches.postValue(response.body())
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.errorBody()}"
                )
            }
        }
    }

    fun saveSearch(search: Search) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository.saveSearch(search)
            if (response.isSuccessful) {
                Log.i("SEARCH", "POST: ${response.body().toString()}")
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.errorBody()}"
                )
            }
        }
    }

    fun deleteSearch(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchRepository.deleteSearch(id)
            if (response.isSuccessful) {
                Log.i("SEARCH", "DELETE: ${response.body().toString()}")
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.errorBody()}"
                )
            }
        }
    }

}