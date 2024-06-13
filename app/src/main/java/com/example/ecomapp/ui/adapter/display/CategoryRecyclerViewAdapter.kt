package com.example.ecomapp.ui.adapter.display

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.Category
import com.example.ecomapp.databinding.ViewholderCategoryItemBinding

class CategoryRecyclerViewAdapter(

    private val categories: List<Category>

) : RecyclerView.Adapter<CategoryViewHolder>() {

    var listCategoryId = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ViewholderCategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val category = categories[position]

        holder.bind(category)

        val isSelected = listCategoryId.contains(category.id)

        holder.itemView.findViewById<TextView>(R.id.tvCategory)
            .setBackgroundResource(if (isSelected) R.drawable.blue_circle_background else R.drawable.white_circle_background)
        holder.itemView.findViewById<TextView>(R.id.tvCategory)
            .setTextColor(if (isSelected) Color.WHITE else Color.BLACK)

        holder.itemView.setOnClickListener {
            if (isSelected) {
                listCategoryId.remove(category.id)
            } else {
                listCategoryId.add(category.id)
            }
            notifyDataSetChanged()
        }
    }

}

class CategoryViewHolder(val binding: ViewholderCategoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Category) {
        binding.apply {
            tvCategory.text = category.categoryName
        }
    }
}