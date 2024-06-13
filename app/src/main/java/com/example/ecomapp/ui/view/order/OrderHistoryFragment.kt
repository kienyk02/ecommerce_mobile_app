package com.example.ecomapp.ui.view.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentOrderHistoryBinding
import com.example.ecomapp.ui.adapter.MyPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OrderHistoryFragment : Fragment() {
    private lateinit var binding: FragmentOrderHistoryBinding
    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.btnSearch.setOnClickListener {
            controller.navigate(R.id.action_orderHistoryFragment_to_searchOrderFragment)
        }

        val fragments: List<Fragment> = listOf(DeliveringFragment(), DeliveredFragment(), VoteFragment())
        val pagerAdapter = MyPagerAdapter(requireActivity(), fragments)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Delivering"
                1 -> tab.text = "Delivered"
                2 -> tab.text = "Rating"
            }
        }.attach()

    }


}