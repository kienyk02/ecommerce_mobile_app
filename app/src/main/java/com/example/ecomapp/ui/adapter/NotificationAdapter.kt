package com.example.ecomapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.data.Notification
import com.example.ecomapp.databinding.FragmentNotificationBinding
import com.example.ecomapp.databinding.ItemNotificationBinding

class NotificationAdapter(
    private var list: MutableList<Notification>,
    private val fragment: FragmentNotificationBinding
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: MutableList<Notification>){
        this.list = newData
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(noti: Notification) {
            binding.apply {
//                val imageBytes = Base64.decode(noti.image, Base64.DEFAULT)
//                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//                imgNoti.setImageBitmap(decodedImage)

                txtNotiContent.text = noti.content
                txtNotiTime.text = noti.time
            }
        }

    }

}