package com.example.androidtecapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.50.84.212:3000"  // Replace with your Go API URL

    // Lazy initialization of Retrofit
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Converts JSON to Kotlin objects
            .build()

        retrofit.create(ApiService::class.java)
    }
}
