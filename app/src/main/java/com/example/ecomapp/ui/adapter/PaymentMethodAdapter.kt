package com.example.ecomapp.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.PaymentMethod
import com.example.ecomapp.databinding.ItemPaymentMethodBinding
import com.example.ecomapp.ui.view.checkout.PaymentMethodFragment

class PaymentMethodAdapter(private var list: List<PaymentMethod>, private var fragment: PaymentMethodFragment) : RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPaymentMethodBinding.inflate(
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
    fun setData(newList:List<PaymentMethod>){
        this.list=newList
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemPaymentMethodBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }
        @SuppressLint("ResourceAsColor")
        fun bind(paymentMethod : PaymentMethod){
            binding.apply {
                txtPaymentMethod.text=paymentMethod.name
                if(paymentMethod.name=="Thanh toán chuyển khoản"){
                    imgPaymentType.setImageResource(R.drawable.payment_banking)
                }
                if (paymentMethod.id==fragment.paymentMethodTmp.id){
                    imgChecked.visibility=View.VISIBLE
                    viewSelect.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primary_color))
                }else{
                    imgChecked.visibility=View.GONE
                    viewSelect.setBackgroundColor(Color.LTGRAY)
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onClick(p0: View?) {
            fragment.updatePaymentMethod(list[adapterPosition])
            notifyDataSetChanged()
        }
    }
}