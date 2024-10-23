package com.example.androidtecapp.network

import com.example.androidtecapp.models.User
import com.example.androidtecapp.network.responses.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/api/user")
    fun createUser(
        @Header("Authorization") token: String,  // Firebase token in the header
        @Body user: User                        // User data in the request body
    ): Call<UserResponse>
}

