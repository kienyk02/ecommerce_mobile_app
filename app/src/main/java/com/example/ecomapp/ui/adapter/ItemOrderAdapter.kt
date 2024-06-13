package com.example.ecomapp.ui.adapter


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.ItemOrder
import com.example.ecomapp.databinding.ItemItemorderBinding

class ItemOrderAdapter(private var list: List<ItemOrder>) :
    RecyclerView.Adapter<ItemOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemItemorderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<ItemOrder>) {
        this.list = newList
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemItemorderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(itemOrder: ItemOrder) {
            binding.apply {
                val imageBytes = Base64.decode(itemOrder.product.image, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imgProduct.setImageBitmap(decodedImage)
                txtNameP.text = itemOrder.product.title
                txtPriceP.text = formatPrice(itemOrder.product.price)
                txtQuantityP.text = "x" + itemOrder.quantity.toString()
                var categories = ""
                for (category in itemOrder.product.categories) {
                    categories += category.categoryName + ", "
                }
                categories = categories.substring(0, categories.length - 2)
                txtTypeP.text = categories

            }
            if (adapterPosition == list.size - 1) {
                var boundary: View = itemView.findViewById(R.id.boundaryLine)
                val layoutParams = boundary.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.marginStart = 0
                boundary.layoutParams = layoutParams
            }
        }
        private fun formatPrice(price: Int): String {
            return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
        }
    }
}