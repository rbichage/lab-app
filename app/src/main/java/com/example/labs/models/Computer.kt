package com.example.labs.models


import com.google.gson.annotations.SerializedName

// for computers, lcd, projector

data class Computer(
        @SerializedName("id") val id: Int,
        @SerializedName("model") val model: String,
        @SerializedName("name") val name: String,
        @SerializedName("serial_no") val serialNo: String,
        @SerializedName("resource_type") val resourceType: String
)

data class BookLab(
        @SerializedName("lab_id") val labId: Int,
        @SerializedName("user_id") val userId: Int
)

data class BookResource(
        @SerializedName("resource_id") val labId: Int,
        @SerializedName("user_id") val userId: Int
)