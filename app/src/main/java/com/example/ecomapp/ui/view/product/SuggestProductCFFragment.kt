package com.example.ecomapp.ui.view.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentSuggestProductCFBinding
import com.example.ecomapp.network.JwtManager
import com.example.ecomapp.ui.adapter.home.SuggestProductCFAdapter
import com.example.ecomapp.ui.viewmodel.SuggestProductCFViewModel

class SuggestProductCFFragment : Fragment() {
    private lateinit var binding:FragmentSuggestProductCFBinding

    private val suggestProductCFViewModel: SuggestProductCFViewModel by lazy {
        ViewModelProvider(
            this
        )[SuggestProductCFViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuggestProductCFBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
        fetchSuggestProductsCF()
    }

    private fun fetchSuggestProductsCF() {
        suggestProductCFViewModel.fetchProductSuggests(JwtManager.CURRENT_USER.toInt(),15)
        val suggestProductCFAdapter = SuggestProductCFAdapter(mutableListOf())
        binding.rvSuggestBooksCF.layoutManager =
            GridLayoutManager(requireContext(), 2)
        binding.rvSuggestBooksCF.adapter = suggestProductCFAdapter
        suggestProductCFViewModel.productSuggests.observe(viewLifecycleOwner, Observer {
            suggestProductCFAdapter.setData(it)
        })
        suggestProductCFAdapter.onItemClick = {
            val bundle = bundleOf("product" to it)
            findNavController().navigate(
                R.id.action_suggestProductCFFragment_to_detailProductFragment,
                bundle
            )
        }
    }
}