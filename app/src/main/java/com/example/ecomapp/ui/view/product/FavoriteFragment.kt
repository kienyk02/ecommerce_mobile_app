package com.example.ecomapp.ui.view.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.Favorite
import com.example.ecomapp.databinding.FragmentFavoriteBinding
import com.example.ecomapp.network.JwtManager
import com.example.ecomapp.ui.adapter.display.DisplayProductRecyclerViewAdapter
import com.example.ecomapp.ui.adapter.display.FavoriteProductRecyclerViewAdapter
import com.example.ecomapp.ui.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteProductRecyclerViewAdapter

    private lateinit var favorites : ArrayList<Favorite>

    private val jwtManager = JwtManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        fetchFavoriteProducts(jwtManager.getUserId(requireContext()).toInt())

        binding.apply {

            ivBackArrow.setOnClickListener {
                it.findNavController().popBackStack()
            }

            ivSearch.setOnClickListener {
                it.findNavController().navigate(R.id.action_favoriteFragment_to_searchFragment)
            }

        }

        return binding.root
    }

    private fun fetchFavoriteProducts(userId: Int) {
        val favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        favoriteViewModel.fetchFavoriteByUserId(userId)
        favoriteViewModel.favorites.observe(viewLifecycleOwner) {
            if (it != null) {
                favorites = it as ArrayList<Favorite>
                addFavoriteToAdapter(favorites)
            }
        }
    }

    private fun deleteFavoriteProduct(id : Int) {
        val favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        favoriteViewModel.deleteFavorite(id)
    }

    private fun addFavoriteToAdapter(favorites: ArrayList<Favorite>) {

        binding.apply {

            favoriteRecyclerView = rvDisplayBook
            favoriteRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            favoriteAdapter = FavoriteProductRecyclerViewAdapter(favorites)
            favoriteRecyclerView.adapter = favoriteAdapter

            favoriteAdapter.onHeartClick = {
                id -> deleteFavoriteProduct(id)
            }

            favoriteAdapter.onItemClick = {
                val bundle = bundleOf("product" to it.product)
                findNavController().navigate(
                    R.id.action_favoriteFragment_to_detailProductFragment,
                    bundle
                )
            }
        }
    }

}