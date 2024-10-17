package com.example.androidtecapp.network

import com.example.androidtecapp.models.User
import com.example.androidtecapp.network.responses.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    // Send the Firebase UID via headers instead of in the body
    @POST("/users")
    fun createUser(
        @Header("Authorization") firebaseUid: String,  // Header to pass the UID
        @Body user: User  // User data without the UID
    ): Call<UserResponse>
}
