package com.example.ecomapp.ui.view.order

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.databinding.FragmentSearchOrderBinding
import com.example.ecomapp.ui.adapter.OrderAdapter
import com.example.ecomapp.ui.viewmodel.OrderViewModel


class SearchOrderFragment : Fragment() {
    private lateinit var binding: FragmentSearchOrderBinding
    private val controller by lazy{
        findNavController()
    }
    private val viewModel: OrderViewModel by lazy {
        ViewModelProvider(this, OrderViewModel.OrderViewModelFactory(requireActivity().application))[OrderViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        val orderAdapter = OrderAdapter(listOf(),this)
        binding.rvOrder.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        binding.rvOrder.adapter = orderAdapter

        viewModel.listOrderSearch.observe(viewLifecycleOwner, Observer { data->
            orderAdapter.setData(data)
            if(data.isNotEmpty()){
                binding.orderNone.visibility= View.GONE
            }else{
                binding.orderNone.visibility= View.VISIBLE
            }
        })

        binding.orderSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            @SuppressLint("SetTextI18n")
            override fun onQueryTextSubmit(query: String?): Boolean {
                val key=binding.orderSearch.query.toString()
                viewModel.searchingOrder(key)
                binding.txtNotiResult.text= "Đơn hàng được tìm kiếm theo từ khoá \"$key\""
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }
}