package com.example.labs.api

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(context: Context): Interceptor{

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = applicationContext.getSharedPreferences("Shared_pref", Context.MODE_PRIVATE).getString("access_token", "")

        Log.e("TOKEN", token)

        val authRequest = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $token")
                .build()

        return chain.proceed(authRequest)
    }

}