package com.example.androidtecapp.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Assistant(
    @SerializedName("UserFBID") val userFBID: String,
    @SerializedName("Username") val username: String,
    @SerializedName("Email") val email: String,
)

