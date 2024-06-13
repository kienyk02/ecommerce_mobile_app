package com.example.ecomapp.ui.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentBankBinding

class BankFragment : Fragment() {

    private lateinit var binding: FragmentBankBinding
    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBankBinding.inflate(inflater, container, false)

        binding.apply {

            btnBack.setOnClickListener {
                controller.popBackStack()
            }

        }

        return binding.root
    }
}