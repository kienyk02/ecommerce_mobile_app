package com.example.ecomapp.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.data.Category
import com.example.ecomapp.data.ItemOrder
import com.example.ecomapp.data.Order
import com.example.ecomapp.data.Payment
import com.example.ecomapp.data.Product
import com.example.ecomapp.data.Shipment
import com.example.ecomapp.data.User
import com.example.ecomapp.repositories.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class OrderViewModel(application: Application) : ViewModel() {
    private val orderRepository: OrderRepository = OrderRepository(application)

    private val _listOrderDelivering = MutableLiveData<List<Order>>()
    val listOrderDelivering: LiveData<List<Order>> = _listOrderDelivering

    private val _listOrderDelivered = MutableLiveData<List<Order>>()
    val listOrderDelivered: LiveData<List<Order>> = _listOrderDelivered

    private val _listOrderSearch = MutableLiveData<List<Order>>()
    val listOrderSearch: LiveData<List<Order>> = _listOrderSearch

    fun fetchAllOrderDelivering() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _listOrderDelivering.postValue(orderRepository.getOrdersByStatus("Đang giao hàng"))
        } catch (e: Exception) {
            Log.d("errorOrder", e.toString())
        }
    }

    fun fetchAllOrderDelivered() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _listOrderDelivered.postValue(orderRepository.getOrdersByStatus("Đã hoàn thành"))
        } catch (e: Exception) {
            Log.d("errorOrder", e.toString())
        }
    }

    fun searchingOrder(key: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _listOrderSearch.postValue(orderRepository.searchingOrder(key))
        } catch (e: Exception) {
            Log.d("errorOrder", e.toString())
            _listOrderSearch.postValue(emptyList())
        }
    }

    fun getOrderById(id: Int) = liveData(Dispatchers.IO) {
        try {
            emit(orderRepository.getOrderById(id))
        } catch (e: Exception) {
            emit(e.toString())
        }
    }

    fun createOrder(order: Order) = liveData(Dispatchers.IO) {
        try {
            emit(orderRepository.createOrder(order))
        } catch (e: Exception) {
            emit(e.toString())
        }
    }

    fun updateOrderStatus(id: Int, status: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            orderRepository.updateOrderStatus(id, status)
        } catch (e: Exception) {
            Log.d("errorCart", e.toString())
        }
    }

    fun cancelOrder(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            orderRepository.cancelOrder(id)
        } catch (e: Exception) {
            Log.d("errorCart", e.toString())
        }
    }


    class OrderViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OrderViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }
}