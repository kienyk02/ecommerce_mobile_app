package com.example.ecomapp.ui.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.data.ItemOrder
import com.example.ecomapp.databinding.ItemVoteBinding
import com.example.ecomapp.ui.view.order.VoteFragment

class VoteAdapter(private var list: List<ItemOrder>,  private var fragment: VoteFragment) : RecyclerView.Adapter<VoteAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVoteBinding.inflate(
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
    fun setData(newList:List<ItemOrder>){
        this.list=newList
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemVoteBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }
        fun bind(item : ItemOrder){
            binding.apply {
                val imageBytes = Base64.decode(item.product.image, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imgProduct.setImageBitmap(decodedImage)
                txtNameP.text = item.product.title
                btnVote.setOnClickListener{
                    fragment.openVoteDialog(item)
                }
            }
        }

        override fun onClick(p0: View?) {
            Toast.makeText(itemView.context,"hehe",Toast.LENGTH_SHORT).show()
        }
    }
}