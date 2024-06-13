package com.example.ecomapp.ui.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentForgotBinding


class ForgotFragment : Fragment() {

    private lateinit var binding: FragmentForgotBinding
    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.txtEmailInput.requestFocus()

        binding.btnContinue.setOnClickListener {
            controller.navigate(R.id.action_forgotFragment_to_verificationFragment)
        }

        return binding.root
    }
}