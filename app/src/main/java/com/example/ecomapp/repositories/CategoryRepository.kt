package com.example.ecomapp.repositories

import com.example.ecomapp.data.Category
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.CategoryApi
import retrofit2.Response

class CategoryRepository {

    private val categoryService = ApiConfig.retrofit.create(CategoryApi::class.java)

    suspend fun fetchCategories(): Response<List<Category>> {
        return categoryService.getCategories();
    }

}