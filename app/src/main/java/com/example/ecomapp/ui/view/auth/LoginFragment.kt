package com.example.ecomapp.ui.view.auth

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.auth.Resource
import com.example.ecomapp.auth.SigninRequest
import com.example.ecomapp.databinding.FragmentLoginBinding
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.JwtManager
import com.example.ecomapp.repositories.AuthRepository
import com.example.ecomapp.ui.view.home.HomeFragment
import com.example.ecomapp.ui.viewmodel.AuthViewModel
import com.example.ecomapp.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val controller by lazy {
        findNavController()
    }
    private val viewModelAuth: AuthViewModel by lazy {
        ViewModelProvider(this, AuthViewModel.AuthViewModelFactory(requireActivity().application))
            .get(AuthViewModel::class.java)
    }
    private val viewModelUser: UserViewModel by lazy {
        ViewModelProvider(this, UserViewModel.UserViewModelFactory(requireActivity().application))
            .get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        JwtManager.CURRENT_USER = JwtManager.getUserId(requireActivity().application)
        JwtManager.CURRENT_JWT = JwtManager.getJwtToken(requireActivity().application)
//        if(JwtManager.CURRENT_JWT != "none"){
//            controller.navigate(R.id.action_loginFragment_to_homeFragment)
//        }
        viewModelUser.user.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    controller.navigate(R.id.action_loginFragment_to_homeFragment)
                }
                is Resource.Failure -> {}
            }
        })
        viewModelUser.getUserInfo()

        binding.progressBar.visibility = View.GONE

        showAndHidePassword()

        binding.btnForgot.setOnClickListener {
            controller.navigate(R.id.action_loginFragment_to_forgotFragment)
        }

        viewModelAuth.signInResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when(it){
                is Resource.Success -> {
                    JwtManager.saveJwtToken(requireActivity().application, it.value.token)
                    JwtManager.CURRENT_JWT = JwtManager.getJwtToken(requireActivity().application)
                    JwtManager.saveUserId(requireActivity().application, it.value.userId.toString())
                    JwtManager.CURRENT_USER = JwtManager.getUserId(requireActivity().application)
                    controller.navigate(R.id.action_loginFragment_to_homeFragment)
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Email hoặc mật khẩu không chính xác!", Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmailInput.text.toString().trim()
            val password = binding.txtPasswordInput.text.toString().trim()
            //add validation sign in information
            val request = SigninRequest(email, password)
            binding.progressBar.visibility = View.VISIBLE
            viewModelAuth.signIn(request)
        }

        binding.btnSignUp.setOnClickListener {
            controller.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        return binding.root
    }

    private fun showAndHidePassword(){
        binding.btnShowPassword.setOnClickListener {
            binding.btnShowPassword.visibility = View.INVISIBLE
            binding.btnHidePassword.visibility = View.VISIBLE
            binding.txtPasswordInput.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding.txtPasswordInput.setSelection(binding.txtPasswordInput.length())
        }

        binding.btnHidePassword.setOnClickListener {
            binding.btnShowPassword.visibility = View.VISIBLE
            binding.btnHidePassword.visibility = View.INVISIBLE
            binding.txtPasswordInput.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding.txtPasswordInput.setSelection(binding.txtPasswordInput.length())
        }
    }

}
