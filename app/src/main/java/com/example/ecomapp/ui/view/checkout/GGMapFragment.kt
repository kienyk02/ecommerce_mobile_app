package com.example.ecomapp.ui.view.checkout

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.appcompat.widget.SearchView

import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentGGMapBinding
import com.example.ecomapp.ui.viewmodel.ShareCheckoutViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class GGMapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentGGMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var LatLng: LatLng
    private lateinit var geocoder: Geocoder
    private val controller by lazy {
        findNavController()
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
        binding = FragmentGGMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        geocoder = Geocoder(requireActivity())
        binding.mapSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val location: String = binding.mapSearch.query.toString()
                var addrList: ArrayList<Address> = ArrayList()
                if (location != "") {
                    try {
                        addrList = geocoder.getFromLocationName(location, 1) as ArrayList<Address>
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (addrList.size > 0) {
                    val address: Address = addrList.get(0)
                    LatLng = LatLng(address.latitude, address.longitude)
                    moveLocation(LatLng)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Can't Find Your Address!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnConfirm.setOnClickListener {
            try {
                val address: Address =
                    geocoder.getFromLocation(LatLng.latitude, LatLng.longitude, 1)!!.get(0)
                shareCheckoutViewModel.updateAddress(address.getAddressLine(0))
                controller.popBackStack()
            }catch (e:IOException){
                Log.d("geocoderError","error")
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        map.let {
            googleMap = it
            // Thiết lập vị trí ban đầu
            shareCheckoutViewModel.address.observe(viewLifecycleOwner, Observer { addr ->
                var address: Address? = null
                LatLng = LatLng(21.0278,105.8342)
                try {
                    if (addr != "Chưa có địa chỉ"){
                        address = geocoder.getFromLocationName(addr, 1)!!.get(0)
                    }else{
                        address = geocoder.getFromLocationName("Hà Nội, Việt Nam", 1)!!.get(0)
                    }
                    LatLng = LatLng(address.latitude, address.longitude)
                }catch (e:IOException){
                    Log.d("geocoderError","error")
                }
            })

            val zoomLevel =
                15f // Độ zoom ban đầu, giá trị từ 1 (zoom nhỏ nhất) đến 20 (zoom lớn nhất)

            // Di chuyển camera đến vị trí ban đầu và set độ zoom
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, zoomLevel))

            // Đặt marker tại vị trí ban đầu
            marker = googleMap.addMarker(MarkerOptions().position(LatLng))!!

            // Lắng nghe sự kiện khi click vào map
            googleMap.setOnMapClickListener { latLng ->
                LatLng = latLng
                moveLocation(LatLng)
            }
        }
    }

    fun moveLocation(latLng: LatLng) {
        marker.remove()
        marker = googleMap.addMarker(
            MarkerOptions().position(latLng).title("" + latLng.latitude + " KG " + latLng.longitude)
        )!!
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }
}

