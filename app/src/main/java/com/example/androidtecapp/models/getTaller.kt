package com.example.androidtecapp.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class getTaller(
    @SerializedName("CourseID") val courseID: String,
    @SerializedName("CollaboratorFBID") val collaboratorFBID: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Pillar") val pillar: String,
    @SerializedName("StartTime") val startTime: Date,
    @SerializedName("EndTime") val endTime: Date,
    @SerializedName("Longitude") val longitude: Double,
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Limit") var limit: Int,
    @SerializedName("AssistantArray") val assistantArray: List<Assistant> = emptyList()
)

