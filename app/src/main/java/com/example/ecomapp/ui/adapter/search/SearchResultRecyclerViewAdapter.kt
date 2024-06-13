package com.example.ecomapp.ui.adapter.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.Search
import com.example.ecomapp.databinding.ViewholderSearchResultItemBinding
import okhttp3.internal.notify
import retrofit2.Response

class SearchResultRecyclerViewAdapter(

    private val results: ArrayList<Search>

) : RecyclerView.Adapter<SearchResultViewHolder>() {

    lateinit var onItemClick: (Search) -> Unit
    lateinit var deleteSearch : (id : Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ViewholderSearchResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultViewHolder(binding, deleteSearch)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {

        val result = results[position]

        holder.bind(result)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(result)
        }

        holder.itemView.findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
            results.remove(result)
            deleteSearch(result.id!!)
            notifyItemRemoved(position)
        }

    }
}

class SearchResultViewHolder(
    private val binding: ViewholderSearchResultItemBinding,
    private val deleteSearch: (id: Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(search: Search) {
        binding.apply {
            tvSearchResult.text = search.searchResult
        }
    }
}