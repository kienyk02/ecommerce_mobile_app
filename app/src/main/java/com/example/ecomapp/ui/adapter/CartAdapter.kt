package com.example.ecomapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.Cart
import com.example.ecomapp.databinding.ItemCartBinding
import com.example.ecomapp.ui.view.cart.CartFragment

class CartAdapter(private var list: List<Cart>, private val fragment: CartFragment) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(
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
    fun setData(newData: List<Cart>) {
        this.list = newData
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(cart: Cart) {
            binding.apply {
                val imageBytes = Base64.decode(cart.product.image, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imgProduct.setImageBitmap(decodedImage)
                txtNameP.text = cart.product.title
                txtPriceP.text = formatPrice(cart.product.price)
                edtQuantity.setText(cart.quantity.toString())
                var categories = ""
                for (category in cart.product.categories) {
                    categories += category.categoryName + ", "
                }
                categories = categories.substring(0, categories.length - 2)
                txtCategoryP.text = categories

                edtQuantity.imeOptions = EditorInfo.IME_ACTION_NONE
                edtQuantity.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                        if (edtQuantity.text.toString() == ""
                            || edtQuantity.text.toString() == "0"
                            || edtQuantity.text.toString() == "00"
                        ) {
                            edtQuantity.setText("1")
                        }
                        val value = edtQuantity.text.toString().toInt()
                        if (value > 0) {
                            fragment.updateCartQuantity(cart.id!!, value)
                        }
                        //Ẩn bàn phím
                        val imm =
                            itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(edtQuantity.windowToken, 0)
                        true
                    } else {
                        false
                    }
                }

                btnPlus.setOnClickListener {
                    val value = cart.quantity
                    if (value < 99) {
                        fragment.updateCartQuantity(cart.id!!, value + 1)
                    }
                }
                btnMinus.setOnClickListener {
                    val value = cart.quantity
                    if (value > 1) {
                        fragment.updateCartQuantity(cart.id!!, value - 1)
                    }
                }
                btnDelete.setOnClickListener {
                    fragment.deleteCartById(cart.id!!)
                }
            }

        }

        private fun formatPrice(price: Int): String {
            return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
        }

    }
}