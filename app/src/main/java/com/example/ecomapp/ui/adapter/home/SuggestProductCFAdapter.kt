package com.example.ecomapp.ui.adapter.home

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.data.Product
import com.example.ecomapp.databinding.ItemSuggestProductCfBinding
import com.example.ecomapp.databinding.ViewholderPopularItemBinding

class SuggestProductCFAdapter(

    private var products: List<Product>

) : RecyclerView.Adapter<ViewHolder>() {

    lateinit var onItemClick: (Product) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSuggestProductCfBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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



class ViewHolder(private val binding: ItemSuggestProductCfBinding) :
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