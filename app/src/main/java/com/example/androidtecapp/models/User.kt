package com.example.androidtecapp.models

data class User(
    val Username: String,
    val Email: String,
)

data class UserResponse(
    val user: User
)