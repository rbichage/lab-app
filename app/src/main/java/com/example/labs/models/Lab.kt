package com.example.labs.models


import com.google.gson.annotations.SerializedName

data class Lab(
    @SerializedName("capacity")
    val capacity: Int,
    @SerializedName("name")
    val name: String
)