package com.example.ecomapp.ui.adapter.display

import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.databinding.ViewholderDisplaybookItemBinding
import android.util.Base64
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import com.example.ecomapp.R
import com.example.ecomapp.data.Favorite
import com.example.ecomapp.databinding.ViewholderFavoriteItemBinding

class FavoriteProductRecyclerViewAdapter(

    private val favorites: ArrayList<Favorite>

) : RecyclerView.Adapter<FavoriteViewHolder>() {

    lateinit var onItemClick: (Favorite) -> Unit
    lateinit var onHeartClick: (id : Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ViewholderFavoriteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

        val favorite = favorites[position]

        holder.bind(favorite)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(favorite)
        }

        holder.itemView.findViewById<ImageView>(R.id.ivHeartIcon).setOnClickListener {
            favorites.remove(favorite)
            onHeartClick.invoke(favorite.id!!)
            notifyItemRemoved(position)
        }


    }
}

class FavoriteViewHolder(
    private val binding: ViewholderFavoriteItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(favorite: Favorite) {

        binding.apply {

            tvTitle.text = favorite.product.title
            tvAuthor.text = favorite.product.author
            tvPrice.text = formatPrice(favorite.product.price)

            ivHeartIcon.setBackgroundResource(R.drawable.baseline_favorite_24)

            val imageBytes = Base64.decode(favorite.product.image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ivBookCover.setImageBitmap(decodedImage)

        }

    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }

}

