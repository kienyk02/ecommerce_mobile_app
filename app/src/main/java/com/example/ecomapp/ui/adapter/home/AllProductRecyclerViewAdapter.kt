package com.example.ecomapp.ui.adapter.home

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.data.Product
import com.example.ecomapp.databinding.ViewholderAllItemBinding

class AllProductRecyclerViewAdapter (

    private val products: List<Product>

) : RecyclerView.Adapter<AllViewHolder>() {

    lateinit var onItemClick: (Product) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolder {
        val binding = ViewholderAllItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AllViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: AllViewHolder, position: Int) {

        val product = products[position]

        holder.bind(product)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(product)
        }

    }
}

class AllViewHolder(private val binding: ViewholderAllItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {

        binding.apply {

            tvTitle.text = product.title
            tvPrice.text = formatPrice(product.price)

            val imageBytes = Base64.decode(product.image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ivBookCover.setImageBitmap(decodedImage)

        }
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }

}