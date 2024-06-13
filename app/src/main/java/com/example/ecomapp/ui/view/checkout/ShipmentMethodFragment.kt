package com.example.ecomapp.ui.view.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.data.ShipmentMethod
import com.example.ecomapp.databinding.FragmentShipmentMethodBinding
import com.example.ecomapp.ui.adapter.ShipmentMethodAdapter
import com.example.ecomapp.ui.viewmodel.ShareCheckoutViewModel

class ShipmentMethodFragment : Fragment() {
    private lateinit var binding: FragmentShipmentMethodBinding
    lateinit var shipmentMethodTmp : ShipmentMethod
    private val controller by lazy {
        findNavController()
    }
    private val shareCheckoutViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ShareCheckoutViewModel.ShareCheckoutViewModelFactory(requireActivity().application)
        )[ShareCheckoutViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShipmentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        val shipmentMethodAdapter = ShipmentMethodAdapter(mutableListOf(), this)
        binding.rvShipmentMethod.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvShipmentMethod.adapter = shipmentMethodAdapter

        shareCheckoutViewModel.shipmentMethods.observe(viewLifecycleOwner, Observer {
            shipmentMethodAdapter.setData(it)
        })

        shipmentMethodTmp= shareCheckoutViewModel.shipmentMethodSelected.value!!
        binding.btnConfirm.setOnClickListener {
            shareCheckoutViewModel.updateShipmentMethod(shipmentMethodTmp)
            controller.popBackStack()
        }

    }

    fun updateShipmentMethod(shipmentMethod: ShipmentMethod){
        shipmentMethodTmp=shipmentMethod
    }
}