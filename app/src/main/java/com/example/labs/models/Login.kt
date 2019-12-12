package com.example.labs.models


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("client_id") val clientId: Int,
    @SerializedName("client_secret") val clientSecret: String,
    @SerializedName("grant_type") val grantType: String,
    @SerializedName("password") val password: String,
    @SerializedName("username") val username: String
)