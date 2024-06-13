package com.example.ecomapp.ui.view.auth

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentRenewPasswordBinding

class RenewPasswordFragment : Fragment() {

    private lateinit var binding: FragmentRenewPasswordBinding
    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRenewPasswordBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.txtNewPasswordInput.requestFocus()

        showAndHidePassword()

        binding.txtNewPasswordAgainInput.addTextChangedListener {
            checkPassword()
        }

        binding.btnConfirm.setOnClickListener {
            Toast.makeText(activity, "Đổi Mật Khẩu Thành Công!", Toast.LENGTH_SHORT).show()
            controller.navigate(R.id.action_renewPasswordFragment_to_loginFragment)
        }

        return binding.root
    }

    private fun showAndHidePassword(){
        binding.btnShowPassword.setOnClickListener {
            binding.btnShowPassword.visibility = View.INVISIBLE
            binding.btnHidePassword.visibility = View.VISIBLE
            binding.txtNewPasswordInput.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding.txtNewPasswordInput.setSelection(binding.txtNewPasswordInput.length())
        }

        binding.btnHidePassword.setOnClickListener {
            binding.btnShowPassword.visibility = View.VISIBLE
            binding.btnHidePassword.visibility = View.INVISIBLE
            binding.txtNewPasswordInput.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding.txtNewPasswordInput.setSelection(binding.txtNewPasswordInput.length())
        }

        binding.btnShowPassword2.setOnClickListener {
            binding.btnShowPassword2.visibility = View.INVISIBLE
            binding.btnHidePassword2.visibility = View.VISIBLE
            binding.txtNewPasswordAgainInput.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding.txtNewPasswordAgainInput.setSelection(binding.txtNewPasswordAgainInput.length())
        }

        binding.btnHidePassword2.setOnClickListener {
            binding.btnShowPassword2.visibility = View.VISIBLE
            binding.btnHidePassword2.visibility = View.INVISIBLE
            binding.txtNewPasswordAgainInput.transformationMethod =
                PasswordTransformationMethod.getInstance()
            binding.txtNewPasswordAgainInput.setSelection(binding.txtNewPasswordAgainInput.length())
        }
    }

    private fun checkPassword(){
        val newPassword = binding.txtNewPasswordInput.text.toString()
        val newPasswordAgain = binding.txtNewPasswordAgainInput.text.toString()
        if(newPassword == newPasswordAgain){
            binding.passwordAlert.visibility = View.INVISIBLE
        } else {
            binding.passwordAlert.visibility = View.VISIBLE
        }
    }

}