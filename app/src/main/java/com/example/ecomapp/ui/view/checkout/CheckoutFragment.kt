package com.example.ecomapp.ui.view.checkout

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.data.ItemOrder
import com.example.ecomapp.data.Order
import com.example.ecomapp.data.Payment
import com.example.ecomapp.data.Product
import com.example.ecomapp.data.Shipment
import com.example.ecomapp.databinding.FragmentCheckoutBinding
import com.example.ecomapp.ui.viewmodel.CartViewModel
import com.example.ecomapp.ui.viewmodel.OrderViewModel
import com.example.ecomapp.ui.viewmodel.ShareCheckoutViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale
import java.util.concurrent.Executor

class CheckoutFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var geocoder: Geocoder
    private var isFirstTimeCreated = true
    private var totalPriceTmp = 0
    private val controller by lazy {
        findNavController()
    }
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider(
            this,
            OrderViewModel.OrderViewModelFactory(requireActivity().application)
        )[OrderViewModel::class.java]
    }
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(this, CartViewModel.CartViewModelFactory(requireActivity().application))
            .get(CartViewModel::class.java)
    }
    private val shareCheckoutViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ShareCheckoutViewModel.ShareCheckoutViewModelFactory(requireActivity().application)
        )[ShareCheckoutViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFirstTimeCreated) {
            shareCheckoutViewModel.fetchUserInfoDefault()
            isFirstTimeCreated = false
        }
        binding.btnBack.setOnClickListener {
            controller.popBackStack()
        }

        binding.refeshUserInfo.setOnClickListener {
            shareCheckoutViewModel.fetchUserInfoDefault()
        }

        binding.btnPayment.setOnClickListener {
            handlePayment()
        }

        binding.btnEditName.setOnClickListener {
            openDialogEditName()
        }

        binding.btnEditPhone.setOnClickListener {
            openDialogEditPhone()
        }

        binding.layoutShipmentMethod.setOnClickListener {
            controller.navigate(R.id.action_checkoutFragment_to_shipmentMethodFragment)
        }

        binding.layoutPaymentMethod.setOnClickListener {
            controller.navigate(R.id.action_checkoutFragment_to_paymentMethodFragment)
        }

        shareCheckoutViewModel.name.observe(viewLifecycleOwner, Observer {
            binding.txtName.text = it
        })

        shareCheckoutViewModel.phone.observe(viewLifecycleOwner, Observer {
            binding.txtPhone.text = it
        })

        shareCheckoutViewModel.shipmentMethodSelected.observe(viewLifecycleOwner, Observer {
            binding.shipmentMethod.text = it.name
        })

        shareCheckoutViewModel.paymentMethodSelected.observe(viewLifecycleOwner, Observer {
            binding.paymentMethod.text = it.name
        })

        cartViewModel.fetchAllCart()
        cartViewModel.listCart.observe(viewLifecycleOwner, Observer {
            var totalPrice = 0
            var quantityProduct = 0
            for (cart in it) {
                totalPrice += cart.product.price * cart.quantity
                quantityProduct+=cart.quantity
            }
            binding.txtSubtotal.text = formatPrice(totalPrice)
            shareCheckoutViewModel.shipmentMethodSelected.observe(
                viewLifecycleOwner,
                Observer { shipMethod ->
                    totalPrice += quantityProduct * shipMethod.price
                    totalPriceTmp = totalPrice
                    binding.txtShiping.text = formatPrice(quantityProduct * shipMethod.price)
                    binding.txtTotal.text = formatPrice(totalPrice)
                })
        })

        geocoder = Geocoder(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.btnMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun handlePayment() {
        if (shareCheckoutViewModel.address.value.equals("Chưa có địa chỉ")) {
            Toast.makeText(requireActivity(), "Vui lòng cung cấp địa chỉ", Toast.LENGTH_SHORT)
                .show()
            return
        } else if (shareCheckoutViewModel.phone.value.equals("")) {
            Toast.makeText(requireActivity(), "Vui lòng cung cấp số điện thoại", Toast.LENGTH_SHORT)
                .show()
            return
        }
        biometricAuthenticate()
    }

    private fun biometricAuthenticate() {
        val manager = BiometricManager.from(requireActivity())

        val authenticators = if (Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG
        val promptInfo = PromptInfo.Builder()
            .setTitle("Authenticate Biometric")
            .setSubtitle("Checkout with biometric")
            .setAllowedAuthenticators(authenticators)
        if (Build.VERSION.SDK_INT < 30) {
            promptInfo.setNegativeButtonText("Cancel")
        }

        when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(
                    requireActivity(),
                    "Thiết bị không làm việc",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(
                    requireActivity(),
                    "Thiết bị không có dấu vẫn tay",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(
                    requireActivity(),
                    "Thiết bị chưa đăng kí xác thực sinh trắc học",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            else -> Unit
        }

        val executor: Executor = ContextCompat.getMainExecutor(requireActivity())
        val biometricPrompt: BiometricPrompt = BiometricPrompt(
            requireActivity(), executor, object : AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    createOrder()
                }
            }
        )
        biometricPrompt.authenticate(promptInfo.build())
    }

    private fun createOrder() {
        val itemOrders: MutableList<ItemOrder> = mutableListOf()
        for (cart in cartViewModel.listCart.value!!) {
            itemOrders.add(
                ItemOrder(
                    product = Product(id = cart.product.id),
                    quantity = cart.quantity
                )
            )
        }
        val newOrder: Order = Order(
            status = "Đang giao hàng",
            address = shareCheckoutViewModel.address.value!!,
            phoneNumber = shareCheckoutViewModel.phone.value!!,
            itemOrders = itemOrders,
            name = shareCheckoutViewModel.name.value!!,
            user = null,
            shipment = Shipment(
                code = "PTIT Express - PTITEX053213030",
                shipStatus = "Chờ lấy hàng",
                shipmentMethod = shareCheckoutViewModel.shipmentMethodSelected.value!!
            ),
            payment = Payment(
                totalPrice = totalPriceTmp,
                payStatus = if (
                    shareCheckoutViewModel.paymentMethodSelected.value!!.name == "Thanh toán trực tiếp"
                )
                    "Chờ thanh toán" else "Đã thanh toán",
                paymentMethod = shareCheckoutViewModel.paymentMethodSelected.value!!
            )
        )
        orderViewModel.createOrder(newOrder).observe(viewLifecycleOwner, Observer {
            if (it is Order) {
                openDialogNotification()
                cartViewModel.deleteCartByUser()
            } else {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        map.let {
            googleMap = it
            shareCheckoutViewModel.address.observe(viewLifecycleOwner, Observer { addr ->
                binding.txtAddress.text = addr
                var address: Address? = null
                var LatLng = LatLng(21.0278,105.8342)
                try {
                    address = if (addr != "Chưa có địa chỉ") {
                        geocoder.getFromLocationName(addr, 1)!!.get(0)
                    } else {
                        geocoder.getFromLocationName("Hà Nội, Việt Nam", 1)!!.get(0)
                    }
                    LatLng = LatLng(address.latitude, address.longitude)
                }catch (e:IOException){
                    Log.d("geocoderError","error")
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 15f))
                googleMap.clear()
                marker = googleMap.addMarker(MarkerOptions().position(LatLng))!!
            })

            googleMap.setOnMapClickListener { latLng ->
                controller.navigate(R.id.action_checkoutFragment_to_GGMapFragment)
            }
        }
    }

    fun openDialogNotification() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_noti_checkout)
        dialog.setCanceledOnTouchOutside(false)

        val window = dialog.window
        if (window == null) {
            return
        }
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))// Để loại bỏ viền màu trắng xung quanh dialog

        var btnBack: Button = dialog.findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            controller.navigate(R.id.action_checkoutFragment_to_orderHistoryFragment)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openDialogEditName() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_edit_name)

        val window = dialog.window
        if (window == null) {
            return
        }
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))// Để loại bỏ viền màu trắng xung quanh dialog

        val btnSave: Button = dialog.findViewById(R.id.btnSave)
        val edtName: EditText = dialog.findViewById(R.id.edtName)
        edtName.setText(binding.txtName.text.toString())
        btnSave.setOnClickListener {
            val name = edtName.text.toString()
            if (name != "") {
                shareCheckoutViewModel.updateName(name)
                dialog.dismiss()
            } else {
                Toast.makeText(requireActivity(), "Vui lòng nhập họ tên", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.show()
    }

    private fun openDialogEditPhone() {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_edit_phonenumber)

        val window = dialog.window
        if (window == null) {
            return
        }
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))// Để loại bỏ viền màu trắng xung quanh dialog

        val btnSave: Button = dialog.findViewById(R.id.btnSave)
        val edtPhone: EditText = dialog.findViewById(R.id.edtPhone)
        edtPhone.setText(binding.txtPhone.text.toString())
        btnSave.setOnClickListener {
            val phone = edtPhone.text.toString()
            if (phone.length == 10) {
                shareCheckoutViewModel.updatePhone(phone)
                dialog.dismiss()
            } else {
                Toast.makeText(requireActivity(), "Please enter all 10 numbers", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dialog.show()
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }
}