package com.example.ecomapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.data.Product
import com.example.ecomapp.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel() : ViewModel() {

    private val productRepository = ProductRepository();

    // Home
    private val _allProducts = MutableLiveData<List<Product>>()
    private val _popularProducts = MutableLiveData<List<Product>>()
    private val _suggestProducts = MutableLiveData<List<Product>>()

    // Display
    private val _displayProduct = MutableLiveData<List<Product>>()

    // Save data in these
    val suggestProducts: MutableLiveData<List<Product>>
        get() = _suggestProducts
    val popularProducts: MutableLiveData<List<Product>>
        get() = _popularProducts
    val allProducts: MutableLiveData<List<Product>>
        get() = _allProducts
    val displayProducts: MutableLiveData<List<Product>>
        get() = _displayProduct

    fun fetchProductsByTypeWithLimit(id: Int, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.fetchProductByTypeWithLimit(id, quantity)
            if (response.isSuccessful) {
                when (id) {
                    0 -> allProducts.postValue(response.body())
                    1 -> popularProducts.postValue(response.body())
                    else -> suggestProducts.postValue(response.body())
                }
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.message()}"
                )
            }
        }
    }

    fun fetchProducts(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (id == 0) {
                val response = productRepository.fetchProducts()
                if (response.isSuccessful) {
                    displayProducts.postValue(response.body())
                } else {
                    Log.i(
                        "Error",
                        "Error: code = ${response.code()} and message = ${response.message()}"
                    )
                }
            } else if (id == 1) {
                val response = productRepository.fetchProductsByType(1)
                if (response.isSuccessful) {
                    displayProducts.postValue(response.body())
                } else {
                    Log.i(
                        "Error",
                        "Error: code = ${response.code()} and message = ${response.message()}"
                    )
                }
            } else {
                val response = productRepository.fetchProductsByType(2)
                if (response.isSuccessful) {
                    displayProducts.postValue(response.body())
                } else {
                    Log.i(
                        "Error",
                        "Error: code = ${response.code()} and message = ${response.message()}"
                    )
                }
            }
        }
    }

    fun fetchProductsFilterByCategoriesAndPrice(
        categoryIds: List<Int>,
        listCount: Int,
        price: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.fetchProductsFilterByCategoriesAndPrice(
                categoryIds,
                listCount,
                price
            )
            if (response.isSuccessful) {
                displayProducts.postValue(response.body())
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.errorBody()?.string()}"
                )
            }
        }
    }

    fun fetchProductsFilterByCategoriesAndPriceAndType(
        typeId: Int,
        categoryIds: List<Int>,
        listCount: Int,
        price: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.fetchProductsFilterByCategoriesAndPriceAndType(
                typeId,
                categoryIds,
                listCount,
                price
            )
            if (response.isSuccessful) {
                displayProducts.postValue(response.body())
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.message()}"
                )
            }
        }
    }

    fun searchProducts(keyword : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.searchProducts(keyword)
            if (response.isSuccessful) {
                displayProducts.postValue(response.body())
            } else {
                Log.i(
                    "Error",
                    "Error: code = ${response.code()} and message = ${response.errorBody()}"
                )
            }
        }
    }

}