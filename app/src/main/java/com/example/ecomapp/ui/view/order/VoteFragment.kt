package com.example.ecomapp.ui.view.order

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.R
import com.example.ecomapp.data.Category
import com.example.ecomapp.data.ItemOrder
import com.example.ecomapp.data.Product
import com.example.ecomapp.data.Rate
import com.example.ecomapp.databinding.FragmentVoteBinding
import com.example.ecomapp.ui.adapter.VoteAdapter
import com.example.ecomapp.ui.viewmodel.OrderViewModel
import com.example.ecomapp.ui.viewmodel.VoteViewModel

class VoteFragment : Fragment() {
    private lateinit var binding: FragmentVoteBinding
    private var list: MutableList<ItemOrder> = mutableListOf()
    private val viewModel: OrderViewModel by lazy {
        ViewModelProvider(
            this,
            OrderViewModel.OrderViewModelFactory(requireActivity().application)
        )[OrderViewModel::class.java]
    }

    private val voteViewModel: VoteViewModel by lazy {
        ViewModelProvider(
            this,
            VoteViewModel.VoteViewModelFactory(requireActivity().application)
        )[VoteViewModel::class.java]
    }

    private lateinit var starViews: List<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentVoteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        voteViewModel.fetchItemOrderVote()
        val itemVoteAdapter = VoteAdapter(list, this)
        binding.rvVote.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvVote.adapter = itemVoteAdapter

        voteViewModel.itemOrders.observe(viewLifecycleOwner, Observer {
            itemVoteAdapter.setData(it)
            if (it.isNotEmpty()){
                binding.voteNone.visibility=View.GONE
            }else{
                binding.voteNone.visibility=View.VISIBLE
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun openVoteDialog(item: ItemOrder) {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_voting)

        val window = dialog.window ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))// Để loại bỏ viền màu trắng xung quanh dialog

        starViews = listOf(
            dialog.findViewById(R.id.star1),
            dialog.findViewById(R.id.star2),
            dialog.findViewById(R.id.star3),
            dialog.findViewById(R.id.star4),
            dialog.findViewById(R.id.star5)
        )
        val txtVoteStatus: TextView = dialog.findViewById(R.id.txtVoteStatus)
        var checked = false
        // Set onClickListener for each star
        var stars = 0
        starViews.forEachIndexed { index, starView ->
            starView.setOnClickListener {
                updateStars(index)
                stars = index + 1
                checked = true
                when (index) {
                    0 -> txtVoteStatus.text = "Tệ"
                    1 -> txtVoteStatus.text = "Không hài lòng"
                    2 -> txtVoteStatus.text = "Bình thường"
                    3 -> txtVoteStatus.text = "Hài lòng"
                    4 -> txtVoteStatus.text = "Tuyệt vời"
                }
            }
        }
        val btnSave: Button = dialog.findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            if (checked) {
                val rate: Rate = Rate(
                    product = Product(item.product.id),
                    user = null,
                    stars =stars
                )
//                voteViewModel.insertVote(rate).observe(viewLifecycleOwner, Observer {
//                    if (it is Rate){
//                        voteViewModel.updateItemOrderByRated(item.id!!)
//                        Toast.makeText(requireActivity(), "Đánh giá thành công!", Toast.LENGTH_SHORT)
//                            .show()
//                        dialog.dismiss()
//                    }else{
//                        Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                })
                voteViewModel.updateItemOrderByRated(item.id!!, stars)
                dialog.dismiss()

            } else {
                Toast.makeText(requireActivity(), "Vui lòng đánh giá!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.show()
    }

    private fun updateStars(selectedIndex: Int) {
        starViews.forEachIndexed { index, starView ->
            if (index <= selectedIndex) {
                starView.setImageResource(R.drawable.star_on)
            } else {
                starView.setImageResource(R.drawable.star_off)
            }
        }
    }


}