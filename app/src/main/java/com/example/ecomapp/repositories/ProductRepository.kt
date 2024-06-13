package com.example.ecomapp.repositories

import com.example.ecomapp.data.Product
import com.example.ecomapp.network.ApiConfig
import com.example.ecomapp.network.api.ProductApi
import retrofit2.Response

class ProductRepository {

    private val productService = ApiConfig.retrofit.create(ProductApi::class.java)

    suspend fun fetchProducts() : Response<List<Product>> {
        return productService.getProducts();
    }

    suspend fun fetchProductByTypeWithLimit(id : Int, quantity : Int) : Response<List<Product>> {
        return productService.getProductsByTypeWithLimit(id, quantity);
    }

    suspend fun fetchProductsByType(id : Int) : Response<List<Product>> {
        return productService.getProductsByType(id);
    }

    suspend fun fetchProductsFilterByCategoriesAndPrice(
        categoryIds: List<Int>,
        listCount: Int,
        price: Int
    ) : Response<List<Product>> {
        return productService.fetchProductsFilterByCategoriesAndPrice(listCount, price, categoryIds.joinToString(","));
    }

    suspend fun fetchProductsFilterByCategoriesAndPriceAndType(
        typeId: Int,
        categoryIds: List<Int>,
        listCount: Int,
        price: Int
    ) : Response<List<Product>> {
        return productService.fetchProductsFilterByCategoriesAndPriceAndType(typeId, listCount, price, categoryIds.joinToString(","));
    }

    suspend fun searchProducts(keyword : String) : Response<List<Product>> {
        return productService.searchProducts(keyword);
    }

}