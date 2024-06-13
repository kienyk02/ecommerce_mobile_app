package com.example.ecomapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.data.Category
import com.example.ecomapp.repositories.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val categoryRepository = CategoryRepository()

    private val _categories = MutableLiveData<List<Category>>()

    val categories: MutableLiveData<List<Category>>
        get() = _categories

    fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = categoryRepository.fetchCategories()
            if (response.isSuccessful) {
                categories.postValue(response.body())
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.message()}"
                )
            }
        }
    }


}