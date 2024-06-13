package com.example.ecomapp.ui.adapter.display

import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.data.Product
import com.example.ecomapp.databinding.ViewholderDisplaybookItemBinding
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup

class DisplayProductRecyclerViewAdapter(

    private val products: List<Product>

) : RecyclerView.Adapter<DisplayViewHolder>() {

    lateinit var onItemClick: (Product) -> Unit
    lateinit var onHeartClick : (Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayViewHolder {
        val binding = ViewholderDisplaybookItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DisplayViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: DisplayViewHolder, position: Int) {

        val product = products[position]

        holder.bind(product)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(product)
        }

    }
}

class DisplayViewHolder(private val binding: ViewholderDisplaybookItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {
        binding.apply {

            tvTitle.text = product.title
            tvAuthor.text = product.author
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

