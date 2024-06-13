package com.example.ecomapp.network.api

import com.example.ecomapp.data.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("/products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("/products/{id}")
    suspend fun getProductById(@Query("id") id: Int): Response<Product>

    @GET("/products/type/{id}/{quantity}")
    suspend fun getProductsByTypeWithLimit(
        @Path("id") id: Int,
        @Path("quantity") limit: Int
    ): Response<List<Product>>

    @GET("/products/type/{id}")
    suspend fun getProductsByType(@Path("id") id: Int): Response<List<Product>>

    @GET("/products/categories/{listCount}/{price}")
    suspend fun fetchProductsFilterByCategoriesAndPrice(
        @Path("listCount") listCount: Int,
        @Path("price") price: Int,
        @Query("categoryId") categoryIds: String
    ): Response<List<Product>>

    @GET("/products/type/{typeId}/categories/{listCount}/{price}")
    suspend fun fetchProductsFilterByCategoriesAndPriceAndType(
        @Path("typeId") typeId : Int,
        @Path("listCount") listCount: Int,
        @Path("price") price: Int,
        @Query("categoryId") categoryIds: String
    ): Response<List<Product>>

    @GET("/products/search/{keyword}")
    suspend fun searchProducts(@Path("keyword") keyword : String) : Response<List<Product>>
}