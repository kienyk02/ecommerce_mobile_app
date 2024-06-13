package com.example.ecomapp.data

import java.io.Serializable
import java.util.Date

data class Order(
    val id: Int? = null,
    val createTime: Date? = null,
    val status: String? = null,
    val address: String,
    val phoneNumber: String,
    val name: String,
    val user: User?,
    val itemOrders: List<ItemOrder>,
    val shipment: Shipment,
    val payment: Payment
) : Serializable