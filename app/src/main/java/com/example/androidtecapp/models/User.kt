package com.example.androidtecapp.models

data class User(
    val UserID: String = "",
    val FBID: String,
    val Username: String,
    val Email: String,
    val Cardboard: Int = 0,
    val Glass: Int = 0,
    val Tetrapack: Int = 0,
    val Plastic: Int = 0,
    val Paper: Int = 0,
    val Metal: Int = 0,
    val MedalTrans: Int = 0,
    val MedalEnergy: Int = 0,
    val MedalConsume: Int = 0,
    val MedalDesecho: Int = 0,
    val Courses: List<String> = emptyList(),
    val CourseIDArray: List<String> = emptyList(),
    val NotificationArray: List<String>? = null
)