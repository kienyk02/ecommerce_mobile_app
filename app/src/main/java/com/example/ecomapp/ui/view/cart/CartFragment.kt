package com.example.ecomapp.ui.view.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentCartBinding
import com.example.ecomapp.ui.adapter.CartAdapter
import com.example.ecomapp.ui.viewmodel.CartViewModel


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val controller by lazy {
        findNavController()
    }
    private val viewModel: CartViewModel by lazy {
        ViewModelProvider(this, CartViewModel.CartViewModelFactory(requireActivity().application))
            .get(CartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.btnCheckout.setOnClickListener {
            if (viewModel.listCart.value!!.isNotEmpty()) {
                controller.navigate(R.id.action_cartFragment_to_checkoutFragment)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Vui lòng thêm sản phẩm vào giỏ hàng!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.fetchAllCart()

        val cartAdapter = CartAdapter(mutableListOf(), this)
        binding.rvCart.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvCart.adapter = cartAdapter

        viewModel.listCart.observe(viewLifecycleOwner, Observer {
            cartAdapter.setData(it)
            var totalPrice = 0
            for (cart in it) {
                totalPrice += cart.product.price * cart.quantity
            }
            binding.txtSubtotal.text = formatPrice(totalPrice)
            binding.txtTotal.text = formatPrice(totalPrice)
            onDataLoaded(cartAdapter)
        })


//        viewModel.getModels().observe(viewLifecycleOwner, Observer {
//            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
//        })

//        viewModel.getTest().observe(viewLifecycleOwner, Observer {
//            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
//            Log.d("AA",it.toString())
//        })
    }

    fun updateCartQuantity(id: Int, quantity: Int) {
        viewModel.updateCartQuantity(id, quantity)
        binding.progressBar.visibility = View.VISIBLE
    }

    fun deleteCartById(id: Int) {
        viewModel.deleteCartById(id)
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onDataLoaded(cartAdapter: CartAdapter) {
        if (cartAdapter.itemCount > 0) {
            binding.progressBar.visibility = View.GONE
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.progressBar.visibility = View.GONE
        }, 3000)
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }

}