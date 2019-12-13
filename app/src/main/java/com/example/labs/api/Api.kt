package com.example.labs.api

import android.content.Context
import android.util.Log
import com.example.labs.models.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {
    companion object {
        private const val BASE_URL = "http://35.158.205.209/clms-core/"


        fun loginRequest(): Api {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY

            }

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(Api::class.java)
        }

        fun nextRequest(context: Context): Api {

            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(object : Interceptor {
                        override fun intercept(chain: Interceptor.Chain): Response {
                            val applicationContext = context.applicationContext

                            val request = chain.request()
                            val token = applicationContext.getSharedPreferences("Shared_pref", Context.MODE_PRIVATE).getString("access_token", "")

                            Log.e("TOKEN", token)

                            val authRequest = request.newBuilder()
                                    .addHeader("Content-Type", "application/json")
                                    .addHeader("Authorization", "Bearer $token")
                                    .build()

                            return chain.proceed(authRequest)
                        }
                    })
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(Api::class.java)
        }
    }

    @POST("oauth/token")
    fun login(@Body login: Login): Call<AccessToken>

    @GET("api/{resourceType}/")
    fun getComputers(@Path("resourceType") resourceType: String): Call<List<Computer>>

    @GET("api/labs")
    fun getLabs(): Call<List<Lab>>

    @GET("api/user")
    fun getUser(): Call<User>

    @POST("api/book-resource")
    fun bookResource(@Body bookResource: BookResource): Call<ResponseBody>

    @GET("api/user-resources/{user_id}")
    fun getBookedResources(@Path("user_id") userId: Int): Call<List<Resource>>

    @POST("api/book-lab")
    fun bookLab(@Body bookLab: BookLab): Call<ResponseBody>

    @GET("api/user-labs/{user_id}")
    fun getBookedLabs(@Path("user_id") userId: Int): Call<List<Lab>>
}