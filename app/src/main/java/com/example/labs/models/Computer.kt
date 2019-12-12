package com.example.labs.models


import com.google.gson.annotations.SerializedName

data class Computer(
    @SerializedName("model") val model: String,
    @SerializedName("name") val name: String,
    @SerializedName("serial_no") val serialNo: String,
    @SerializedName("resource_type") val resourceType: String
)