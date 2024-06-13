package com.example.ecomapp.network.api

import com.example.ecomapp.data.ShipmentMethod
import retrofit2.http.GET

interface ShipmentMethodApi {
    @GET("api/shipmentmethods")
    suspend fun getShipmentMethods(): List<ShipmentMethod>
}