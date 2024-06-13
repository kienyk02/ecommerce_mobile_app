package com.example.ecomapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.data.Product
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.ProductSuggestCFApi
import com.example.ecomapp.repositories.SuggestProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuggestProductCFViewModel : ViewModel() {
    private val suggestProductRepository:SuggestProductRepository=SuggestProductRepository()

    private val _productSuggests = MutableLiveData<List<Product>>()
    val productSuggests: LiveData<List<Product>> = _productSuggests

    fun fetchProductSuggests(userId: Int, quantity: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _productSuggests.postValue(suggestProductRepository.getProductsSuggestWithCF(userId, quantity))
        } catch (e: Exception) {
            Log.d("errorProductSuggest", e.toString())
        }
    }
}