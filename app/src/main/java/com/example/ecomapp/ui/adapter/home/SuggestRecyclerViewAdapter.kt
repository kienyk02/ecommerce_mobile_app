package com.example.ecomapp.ui.adapter.home

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.data.Product
import com.example.ecomapp.databinding.ViewholderSuggestItemBinding


class SuggestProductRecyclerViewAdapter (

    private var products: List<Product>

) : RecyclerView.Adapter<SuggestViewHolder>() {

    lateinit var onItemClick: (Product) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestViewHolder {
        val binding = ViewholderSuggestItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SuggestViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: SuggestViewHolder, position: Int) {
        val product = products[position]

        holder.bind(product)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(product)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Product>){
        this.products=newList
        this.notifyDataSetChanged()
    }
}

class SuggestViewHolder(private val binding: ViewholderSuggestItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product) {

        binding.apply {

            tvTitle.text = product.title
            tvAuthor.text = product.author
            tvPrice.text = formatPrice(product.price)

            val imageBytes = Base64.decode(product.image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ivBookCover.setImageBitmap(decodedImage)

//            val resourceId = itemView.context.resources.getIdentifier(
//                product.image,
//                "drawable",
//                itemView.context.packageName
//            )
//
//            ivBookCover.setImageResource(resourceId)

        }
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }

}