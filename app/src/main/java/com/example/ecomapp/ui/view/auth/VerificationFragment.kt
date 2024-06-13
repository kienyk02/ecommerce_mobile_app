package com.example.ecomapp.ui.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentVerificationBinding

class VerificationFragment : Fragment() {

    private lateinit var binding: FragmentVerificationBinding
    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerificationBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.txtCodeInput.requestFocus()

        binding.btnConfirm.setOnClickListener {
            controller.navigate(R.id.action_verificationFragment_to_renewPasswordFragment)
        }

        return binding.root
    }

}