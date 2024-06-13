package com.example.ecomapp.ui.view.order

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecomapp.data.ItemOrder
import com.example.ecomapp.data.Order
import com.example.ecomapp.data.Product
import com.example.ecomapp.databinding.FragmentOrderDetailBinding
import com.example.ecomapp.ui.adapter.ItemOrderAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class OrderDetailFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailBinding
    private val controller by lazy {
        findNavController()
    }
    private var list: MutableList<ItemOrder> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        val itemOrderAdapter = ItemOrderAdapter(list)
        binding.rvItemOrders.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvItemOrders.adapter = itemOrderAdapter

        val bundle = arguments
        val order: Order = bundle?.getSerializable("order") as Order

        itemOrderAdapter.setData(order.itemOrders)

        binding.apply {
            txtOrderStatus.text = order.status
            //Shipment
            deliveryType.text = order.shipment.shipmentMethod.name
            deliveryCode.text = order.shipment.code
            deliveryStatus.text = order.shipment.shipStatus
            //Delivery information
            customerName.text = order.name
            customerPhone.text = order.phoneNumber
            customerAddress.text = order.address
            //payment
            paymentMethod.text = order.payment.paymentMethod.name
            //total price
            txtTotalPrice.text = formatPrice(order.payment.totalPrice)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi", "VN")) // Định nghĩa định dạng ngày
            val dateString = order.createTime?.let { dateFormat.format(it) }
            createTime.text=dateString
        }
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }

}