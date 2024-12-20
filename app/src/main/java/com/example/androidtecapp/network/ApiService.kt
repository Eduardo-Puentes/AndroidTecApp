package com.example.androidtecapp.network

import com.example.androidtecapp.getRecolecta
import com.example.androidtecapp.models.User
import com.example.androidtecapp.models.getTaller
import com.example.androidtecapp.network.responses.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class TopTenUser(
    val UserFBID: String,
    val Username: String,
    val Email: String,
    val Place: Int
)

data class TopTenArray(
    val UserArray: List<TopTenUser>
)


interface ApiService {
    @GET("/api/user/{uid}")
    fun getUser(@Path("uid") uid: String): Call<UserResponse>

    @POST("/api/user")
    fun createUser(
        @Body user: User
    ): Call<UserResponse>

    @GET("/api/course")
    fun getAllCourses(): Call<List<getTaller>>

    @GET("/api/recollect")
    fun getAllRecolectas(): Call<List<getRecolecta>>

    @GET("/api/topten")
    fun getTopTen(): Call<TopTenArray>
}

