package com.example.ecomapp.data

data class Shipment(
    val id: Int? = null,
    val code: String,
    val shipStatus: String,
    val shipmentMethod: ShipmentMethod
)