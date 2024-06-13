package com.example.ecomapp.ui.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val controller by lazy{
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        binding.apply {

            btnBack.setOnClickListener {
                controller.popBackStack()
            }

            btnProfile.setOnClickListener {
                controller.navigate(R.id.action_settingFragment_to_profileFragment)
            }

            btnAddress.setOnClickListener {
                controller.navigate(R.id.action_settingFragment_to_addressFragment)
            }

            btnPayment.setOnClickListener {
                controller.navigate(R.id.action_settingFragment_to_bankFragment)
            }

        }

        return binding.root
    }

}