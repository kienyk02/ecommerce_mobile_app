package com.example.ecomapp.ui.view.order

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.databinding.FragmentDeliveringBinding
import com.example.ecomapp.ui.adapter.OrderAdapter
import com.example.ecomapp.ui.viewmodel.OrderViewModel

class DeliveringFragment:Fragment() {
    private lateinit var binding: FragmentDeliveringBinding
    private val viewModel: OrderViewModel by lazy {
        ViewModelProvider(this, OrderViewModel.OrderViewModelFactory(requireActivity().application))[OrderViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentDeliveringBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Truy cập layout của Fragment

        binding.progressBar.visibility = View.VISIBLE

        viewModel.fetchAllOrderDelivering()

        val orderDeliveringAdapter = OrderAdapter(listOf(),OrderHistoryFragment())
        binding.rvDelivering.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        binding.rvDelivering.adapter = orderDeliveringAdapter

        viewModel.listOrderDelivering.observe(viewLifecycleOwner, Observer { data->
            orderDeliveringAdapter.setData(data)
            if(data.isNotEmpty()){
                binding.orderNone.visibility=View.GONE
            }else{
                binding.orderNone.visibility= View.VISIBLE
            }
            onDataLoaded(orderDeliveringAdapter)
        })

    }

    private fun onDataLoaded(orderDeliveringAdapter: OrderAdapter) {
        if (orderDeliveringAdapter.itemCount > 0) {
            binding.progressBar.visibility = View.GONE
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.progressBar.visibility = View.GONE
        }, 3000)
    }

}