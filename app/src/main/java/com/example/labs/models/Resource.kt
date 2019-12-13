package com.example.labs.models


import com.google.gson.annotations.SerializedName

data class Resource(
    @SerializedName("date_booked")
    val dateBooked: String,
    @SerializedName("resource")
    val resource: String,
    @SerializedName("resource_id")
    val resourceId: Int,
    @SerializedName("user_name")
    val userName: String
)