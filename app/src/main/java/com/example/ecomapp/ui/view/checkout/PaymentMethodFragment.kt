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
import com.example.ecomapp.data.PaymentMethod
import com.example.ecomapp.databinding.FragmentPaymentMethodBinding
import com.example.ecomapp.ui.adapter.PaymentMethodAdapter
import com.example.ecomapp.ui.viewmodel.ShareCheckoutViewModel


class PaymentMethodFragment : Fragment() {
    private lateinit var binding:FragmentPaymentMethodBinding
    lateinit var paymentMethodTmp : PaymentMethod
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
        binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        val paymentMethodAdapter = PaymentMethodAdapter(mutableListOf(), this)
        binding.rvPaymentMethod.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvPaymentMethod.adapter = paymentMethodAdapter

        shareCheckoutViewModel.paymentMethods.observe(viewLifecycleOwner, Observer {
            paymentMethodAdapter.setData(it)
        })

        paymentMethodTmp= shareCheckoutViewModel.paymentMethodSelected.value!!
        binding.btnConfirm.setOnClickListener {
            shareCheckoutViewModel.updatePaymentMethod(paymentMethodTmp)
            controller.popBackStack()
        }

    }

    fun updatePaymentMethod(paymentMethod: PaymentMethod){
        paymentMethodTmp=paymentMethod
    }
}