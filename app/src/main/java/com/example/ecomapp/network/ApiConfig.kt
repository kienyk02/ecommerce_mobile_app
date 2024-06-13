package com.example.ecomapp.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream


object ApiConfig {

    private const val IP = "192.168.2.108"
//    192.168.165.221
    private const val BASE_URL = "http://${IP}:8080/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitWithoutJwt: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAICFServer: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.2.108:5001/")
//        .client(clientSGAI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun createOkHttpClient(): OkHttpClient {

        val httpClient = OkHttpClient.Builder()

        // Thêm interceptor để thêm JWT vào header của mỗi yêu cầu
        httpClient.addInterceptor { chain ->

            val original = chain.request()
            val requestBuilder = original.newBuilder()

            // Thêm JWT vào header nếu có
            JwtManager.CURRENT_JWT.let { jwtToken ->
                Log.d("AA", jwtToken)
                requestBuilder.header("Authorization", "Bearer $jwtToken")
            }

            val request = requestBuilder.build()
            chain.proceed(request)

        }

        return httpClient.build()
    }

    //    val clientSGAI = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//            val request = chain.request()
//            val response = chain.proceed(request)
//            // Kiểm tra xem phản hồi có được nén không
//            if (response.header("Content-Encoding") == "gzip") {
//                val body = response.body
//                if (body != null) {
//                    // Giải nén dữ liệu
//                    val gzippedInputStream = GZIPInputStream(body.byteStream())
//                    val decompressedBuffer = ByteArray(8192)
//                    val decompressedOutputStream = ByteArrayOutputStream()
//                    var read: Int
//                    while (gzippedInputStream.read(decompressedBuffer).also { read = it } != -1) {
//                        decompressedOutputStream.write(decompressedBuffer, 0, read)
//                    }
//                    // Tạo một ResponseBody mới từ dữ liệu đã giải nén
//                    val decompressedResponseBody = ResponseBody.create("application/json".toMediaType(), decompressedOutputStream.toByteArray())
//                    // Tạo một phản hồi mới với dữ liệu đã giải nén
//                    val decompressedResponse = response.newBuilder().body(decompressedResponseBody).build()
//                    return@addInterceptor decompressedResponse
//                }
//            }
//            // Nếu phản hồi không được nén, hoặc không có dữ liệu để giải nén, trả về phản hồi ban đầu
//            return@addInterceptor response
//        }
//        .build()
}