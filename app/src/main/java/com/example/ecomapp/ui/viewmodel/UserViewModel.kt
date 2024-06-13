package com.example.ecomapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.auth.MessageResponse
import com.example.ecomapp.auth.Resource
import com.example.ecomapp.data.User
import com.example.ecomapp.repositories.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.IllegalArgumentException

class UserViewModel(application: Application): ViewModel() {

    private val userRepository: UserRepository = UserRepository(application)

    var _user = MutableLiveData<Resource<User>>()
    var user: LiveData<Resource<User>> = _user

    var responseUpdateUser = MutableLiveData<MessageResponse>()
    var responseUpdateAddress = MutableLiveData<MessageResponse>()

    fun getUserInfo() {
        viewModelScope.launch {
            _user.value = userRepository.getUserInfo()
        }
    }

    fun updateUserInfo(name: RequestBody, gender: RequestBody, phoneNumber: RequestBody, avatarImage: MultipartBody.Part){
        viewModelScope.launch {
            responseUpdateUser.value = userRepository.updateUserInfo(name, gender, phoneNumber, avatarImage)
        }
    }

    fun updateAddress(address: RequestBody){
        viewModelScope.launch {
            responseUpdateAddress.value = userRepository.updateAddress(address)
        }
    }

    class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }

}