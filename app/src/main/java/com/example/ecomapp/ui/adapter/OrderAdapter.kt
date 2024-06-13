package com.example.ecomapp.ui.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.Order
import com.example.ecomapp.databinding.ItemOrderBinding
import com.example.ecomapp.ui.view.order.OrderHistoryFragment
import com.example.ecomapp.ui.view.order.SearchOrderFragment
import android.util.Base64
import android.util.Log

class OrderAdapter(private var list: List<Order>, private var fragment: Fragment) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(
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
    fun setData(newData: List<Order>) {
        this.list = newData
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(order: Order) {
            binding.apply {
                val imageBytes = Base64.decode(order.itemOrders[0].product.image, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imgProduct.setImageBitmap(decodedImage)
                txtNameP.text = order.itemOrders[0].product.title
                txtPriceP.text = formatPrice(order.itemOrders[0].product.price)
                txtQuantityP.text = "x" + order.itemOrders[0].quantity.toString()
                var categories = ""
                for (category in order.itemOrders[0].product.categories) {
                    categories += category.categoryName + ", "
                }
                categories = categories.substring(0, categories.length - 2)
                txtTypeP.text = categories
                txtTotalPrice.text = formatPrice(order.payment.totalPrice)
                if (order.itemOrders.size == 1) {
                    seeMore.visibility = View.GONE
                    lineSeeMode.visibility = View.GONE
                }
                var totalQuantity = 0
                for (item in order.itemOrders) {
                    totalQuantity += item.quantity
                }
                productQuantity.text = totalQuantity.toString() + " sản phẩm"
            }
        }

        override fun onClick(v: View) {
            val bundle = bundleOf(
                "order" to list[adapterPosition]
            )
            if (fragment is OrderHistoryFragment) {
                itemView.findNavController()
                    .navigate(R.id.action_orderHistoryFragment_to_orderDetailFragment, bundle)
            } else if (fragment is SearchOrderFragment) {
                itemView.findNavController()
                    .navigate(R.id.action_searchOrderFragment_to_orderDetailFragment, bundle)
            }
        }

        private fun formatPrice(price: Int): String {
            return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
        }
    }
}