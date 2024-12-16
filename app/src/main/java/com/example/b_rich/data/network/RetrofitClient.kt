package com.example.b_rich.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

<<<<<<< HEAD
    private const val BASE_URL = "http://192.168.1.15:3000/"
=======
    private const val BASE_URL = "http://192.168.242.164:3000/"
>>>>>>> f1034cc (corriger des bugs dans send money)
    private val authInterceptor = AuthInterceptor()


    private val retrofit by lazy {
        val client = OkHttpClient.Builder().addInterceptor(authInterceptor).build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}