package com.example.ecomapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.auth.MessageResponse
import com.example.ecomapp.auth.Resource
import com.example.ecomapp.auth.SigninRequest
import com.example.ecomapp.auth.SignupRequest
import com.example.ecomapp.auth.TokenResponse
import com.example.ecomapp.repositories.AuthRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AuthViewModel(application: Application) : ViewModel() {

    private val authRepository: AuthRepository = AuthRepository(application)

    var signInResponse = MutableLiveData<Resource<TokenResponse>>()
    var signUpResponse = MutableLiveData<Resource<MessageResponse>>()

    fun signIn(request: SigninRequest){
        viewModelScope.launch {
            signInResponse.value = authRepository.signIn(request)
        }
    }

    fun signUp(request: SignupRequest){
        viewModelScope.launch {
            signUpResponse.value = authRepository.signUp(request)
        }
    }

    class AuthViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(application) as T
            }
            throw IllegalArgumentException("sss")
        }
    }

}