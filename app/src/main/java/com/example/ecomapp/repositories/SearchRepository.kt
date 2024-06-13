package com.example.ecomapp.repositories

import com.example.ecomapp.data.Search
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.SearchApi
import retrofit2.Response

class SearchRepository {

    private val searchService = ApiConfig.retrofit.create(SearchApi::class.java)

    suspend fun fetchSearchByUserId(userId : Int) : Response<List<Search>> {
        return searchService.getSearchByUserId(userId)
    }

    suspend fun saveSearch(search: Search) : Response<Search> {
        return searchService.saveSearch(search)
    }

    suspend fun deleteSearch(id: Int): Response<Search> {
        return searchService.deleteSearch(id)
    }

}