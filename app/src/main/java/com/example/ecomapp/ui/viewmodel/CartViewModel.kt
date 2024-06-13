package com.example.ecomapp.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.R
import com.example.ecomapp.data.Cart
import com.example.ecomapp.data.Category
import com.example.ecomapp.data.Product
import com.example.ecomapp.repositories.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CartViewModel(application: Application) : ViewModel() {
    private val cartRepository: CartRepository = CartRepository(application)
    var listCart = MutableLiveData<List<Cart>>()

    init {
        listCart.value = listOf()
    }

    fun fetchAllCart() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listCart.postValue(cartRepository.getAllCart())
            } catch (e: Exception) {
                Log.d("errorCart", e.toString())
            }
        }
    }

    fun addCart(cart: Cart) = liveData(Dispatchers.IO) {
        try {
            emit(cartRepository.addCart(cart))
        } catch (e: Exception) {
            emit(e.toString())
        }
    }

    fun updateCartQuantity(id: Int, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cartRepository.updateCartQuantity(id, quantity)
                fetchAllCart()
            } catch (e: Exception) {
                Log.d("errorCart", e.toString())
            }
        }
    }

    fun deleteCartById(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cartRepository.deleteCartById(id)
                fetchAllCart()
            } catch (e: Exception) {
                Log.d("errorCart", e.toString())
            }
        }
    }
    fun deleteCartByUser(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                cartRepository.deleteCartByUser()
                fetchAllCart()
            } catch (e: Exception) {
                Log.d("errorCart", e.toString())
            }
        }
    }

    fun getModels() = liveData(Dispatchers.IO) {
        try {
            emit(cartRepository.getModels())
        } catch (e: Exception) {
            emit(e.toString())
        }
    }

    fun getTest() = liveData(Dispatchers.IO) {
        try {
            emit(cartRepository.getTest())
        } catch (e: Exception) {
            emit(e.toString())
        }
    }


    class CartViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }

}