package com.example.ecomapp.ui.adapter.home

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.data.Product
import com.example.ecomapp.databinding.ViewholderPopularItemBinding

class PopularProductRecyclerViewAdapter(

    private val products: List<Product>

) : RecyclerView.Adapter<PopularViewHolder>() {

    lateinit var onItemClick: (Product) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = ViewholderPopularItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopularViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val product = products[position]

        holder.bind(product)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(product)
        }

    }
}

class PopularViewHolder(private val binding: ViewholderPopularItemBinding) :
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