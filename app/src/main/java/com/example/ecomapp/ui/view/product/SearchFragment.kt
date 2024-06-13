package com.example.ecomapp.ui.view.product

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.Search
import com.example.ecomapp.data.User
import com.example.ecomapp.databinding.FragmentSearchBinding
import com.example.ecomapp.network.JwtManager
import com.example.ecomapp.ui.adapter.search.SearchResultRecyclerViewAdapter
import com.example.ecomapp.ui.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var myRecyclerView: RecyclerView
    private lateinit var searchResultAdapter: SearchResultRecyclerViewAdapter

    private lateinit var searches : ArrayList<Search>
    private lateinit var keyword : String

    private val jwtManager = JwtManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        fetchSearchByUserId(jwtManager.getUserId(requireContext()).toInt())

        binding.apply {

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    keyword = etSearch.text.toString()

                    val userId = User(id = jwtManager.getUserId(requireContext()).toInt())
                    val search = Search(user = userId, searchResult = keyword)
                    saveSearch(search)

                    val bundle = bundleOf("type" to "3", "keyword" to keyword)
                    findNavController().navigate(
                        R.id.action_searchFragment_to_displayProductFragment,
                        bundle
                    )
                    true
                }
                else {
                    false
                }
            }

            ivBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addSearchToRecyclerView(searches: ArrayList<Search>) {

        binding.apply {

            myRecyclerView = rvSearchResult

            myRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            searchResultAdapter = SearchResultRecyclerViewAdapter(searches)

            myRecyclerView.adapter = searchResultAdapter

            searchResultAdapter.onItemClick = {
                val bundle = bundleOf("type" to "3", "keyword" to it.searchResult)
                findNavController().navigate(
                    R.id.action_searchFragment_to_displayProductFragment,
                    bundle
                )
            }

            searchResultAdapter.deleteSearch = {
                id -> deleteSearch(id)
            }

        }
    }

    private fun fetchSearchByUserId(userId: Int) {

        val searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        searchViewModel.fetchSearchByUserId(userId);

        searchViewModel.searches.observe(viewLifecycleOwner) {
            if (it != null) {
                searches = it as ArrayList<Search>
                addSearchToRecyclerView(searches)
            }
        }
    }

    private fun saveSearch(search: Search) {
        val searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        searchViewModel.saveSearch(search);
    }

    private fun deleteSearch(id: Int) {
        val searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        searchViewModel.deleteSearch(id);
    }

}