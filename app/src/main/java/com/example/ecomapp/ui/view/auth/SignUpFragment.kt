package com.example.ecomapp.ui.view.auth

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.auth.Resource
import com.example.ecomapp.auth.SignupRequest
import com.example.ecomapp.databinding.FragmentSignUpBinding
import com.example.ecomapp.network.JwtManager
import com.example.ecomapp.ui.view.home.HomeFragment
import com.example.ecomapp.ui.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val controller by lazy {
        findNavController()
    }
    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this, AuthViewModel.AuthViewModelFactory(requireActivity().application))
            .get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.progressBar.visibility = View.GONE

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        val itemSpinner = arrayOf("Nam", "Nữ")
        val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, itemSpinner)
        binding.spinnerGender.adapter = spinnerAdapter

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

        viewModel.signUpResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when(it){
                is Resource.Success -> {
                    Toast.makeText(requireContext(), it.value.message, Toast.LENGTH_LONG).show()
                    controller.popBackStack()
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Email đã được sử dụng!", Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.btnSignUp.setOnClickListener {
            val name = binding.txtNameInput.text.toString().trim()
            val gender = binding.spinnerGender.selectedItem.toString().trim()
            val email = binding.txtEmailInput.text.toString().trim()
            val password = binding.txtPasswordInput.text.toString().trim()

            val request = SignupRequest(name, gender, email, password)
            binding.progressBar.visibility = View.VISIBLE
            viewModel.signUp(request)

        }

        binding.btnSignIn.setOnClickListener {
            controller.popBackStack()
        }

        return binding.root
    }

}