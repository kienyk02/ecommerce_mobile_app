package com.example.ecomapp.data

import java.io.Serializable

data class Product(
    val id: Int,
    val title: String? = null,
    val author: String? = null,
    val description: String? = null,
    val rating: Int = 0,
    val price: Int = 0,
    val type: Int = 0,
    val image: String? = null,
    val clickAfterSuggestByAI: Int = 0,
    val categories: List<Category> = listOf()
) : Serializable