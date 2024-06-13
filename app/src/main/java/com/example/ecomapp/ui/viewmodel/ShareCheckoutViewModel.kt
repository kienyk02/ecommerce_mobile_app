package com.example.ecomapp.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.auth.Resource
import com.example.ecomapp.data.PaymentMethod
import com.example.ecomapp.data.ShipmentMethod
import com.example.ecomapp.data.User
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.PaymentMethodApi
import com.example.ecomapp.network.api.ShipmentMethodApi
import com.example.ecomapp.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ShareCheckoutViewModel(application: Application) : ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)
    private val paymentMethodApi: PaymentMethodApi =
        ApiConfig.retrofit.create(PaymentMethodApi::class.java)
    private val shipmentMethodApi: ShipmentMethodApi =
        ApiConfig.retrofit.create(ShipmentMethodApi::class.java)

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> = _phone

    private val _paymentMethodSelected = MutableLiveData<PaymentMethod>()
    val paymentMethodSelected: LiveData<PaymentMethod> = _paymentMethodSelected

    private val _shipmentMethodSelected = MutableLiveData<ShipmentMethod>()
    val shipmentMethodSelected: LiveData<ShipmentMethod> = _shipmentMethodSelected

    var shipmentMethods = MutableLiveData<List<ShipmentMethod>>()
    var paymentMethods = MutableLiveData<List<PaymentMethod>>()

    init {
        initCheckoutInfo()
    }

    fun initCheckoutInfo() {
        fetchPaymentMethods()
        fetchShipmentMethods()
    }

    fun fetchUserInfoDefault() = viewModelScope.launch(Dispatchers.IO) {
        try {
            when(val userInfo : Resource<User> = userRepository.getUserInfo()){
                is Resource.Success -> {
                    _name.postValue(userInfo.value.name!!)
                    _phone.postValue(userInfo.value.phoneNumber!!)
                    _address.postValue(userInfo.value.address!!.address)
                }
                is Resource.Failure -> {
                }
            }
        }catch (e:Exception){
            Log.d("errorUser",e.toString())
        }
    }

    private fun fetchShipmentMethods() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val shipMethods = shipmentMethodApi.getShipmentMethods()
            shipmentMethods.postValue(shipMethods)
            _shipmentMethodSelected.postValue(shipMethods[0])
        } catch (e: Exception) {
            Log.d("errorShipment", e.toString())
        }
    }

    private fun fetchPaymentMethods() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val payMethods = paymentMethodApi.getPaymentMethods()
            paymentMethods.postValue(payMethods)
            _paymentMethodSelected.postValue(payMethods[0])
        } catch (e: Exception) {
            Log.d("errorPayment", e.toString())
        }
    }

    fun updateAddress(addr: String) {
        _address.postValue(addr)
    }

    fun updateName(name: String) {
        _name.postValue(name)
    }

    fun updatePhone(phone: String) {
        _phone.postValue(phone)
    }

    fun updatePaymentMethod(paymentMethod: PaymentMethod) {
        _paymentMethodSelected.postValue(paymentMethod)
    }

    fun updateShipmentMethod(shipmentMethod: ShipmentMethod) {
        _shipmentMethodSelected.postValue(shipmentMethod)
    }

    class ShareCheckoutViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShareCheckoutViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShareCheckoutViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }
}