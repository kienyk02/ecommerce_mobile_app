package com.example.ecomapp.ui.viewmodel;

import android.app.Application;
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.data.ItemOrder
import com.example.ecomapp.data.Rate
import com.example.ecomapp.repositories.ItemOrderRepository
import com.example.ecomapp.repositories.RateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

public class VoteViewModel(application: Application) : ViewModel() {
    private val itemOrderRepository: ItemOrderRepository = ItemOrderRepository(application)
    private val rateRepository: RateRepository = RateRepository(application)

    private val _itemOrders = MutableLiveData<List<ItemOrder>>()
    val itemOrders: LiveData<List<ItemOrder>> = _itemOrders

    fun fetchItemOrderVote() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _itemOrders.postValue(itemOrderRepository.getItemOrderByRated())
        } catch (e: Exception) {
            Log.d("errorItemVote", e.toString())
        }
    }

    fun updateItemOrderByRated(id:Int, stars:Int) = viewModelScope.launch(Dispatchers.IO) {
        try {
            itemOrderRepository.updateItemOrderByRated(id, stars)
            fetchItemOrderVote()
        } catch (e: Exception) {
            Log.d("errorItemVote", e.toString())
        }
    }

    fun insertVote(rate: Rate) = liveData(Dispatchers.IO) {
        try {
            emit(rateRepository.insertRate(rate))
        } catch (e: Exception) {
            emit(e.toString())
        }
    }

    class VoteViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return VoteViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }
}
