package com.example.ecomapp.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.ShipmentMethod
import com.example.ecomapp.databinding.ItemShipmentMethodBinding
import com.example.ecomapp.ui.view.checkout.ShipmentMethodFragment

class ShipmentMethodAdapter(private var list: List<ShipmentMethod>, private var fragment: ShipmentMethodFragment) : RecyclerView.Adapter<ShipmentMethodAdapter.ViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShipmentMethodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList:List<ShipmentMethod>){
        this.list=newList
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemShipmentMethodBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }
        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bind(shipmentMethod: ShipmentMethod){
            binding.apply {
                txtShipmentMethod.text=shipmentMethod.name
                txtShipmentMethodPrice.text=formatPrice(shipmentMethod.price)
                if (shipmentMethod.id==fragment.shipmentMethodTmp.id){
                    imgChecked.visibility= View.VISIBLE
                    viewSelect.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primary_color))
                }else{
                    imgChecked.visibility= View.GONE
                    viewSelect.setBackgroundColor(Color.LTGRAY)
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            fragment.updateShipmentMethod(list[adapterPosition])
            notifyDataSetChanged()
        }

        private fun formatPrice(price: Int): String {
            return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
        }
    }
}