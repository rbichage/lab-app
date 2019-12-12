package com.example.labs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.edit
import com.example.labs.api.Api
import com.example.labs.models.AccessToken
import com.example.labs.models.Login
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isLoggedIn = getSharedPreferences("Shared_pref", Context.MODE_PRIVATE).getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        bLogin.setOnClickListener {
            val email = etUsername.text.toString().trim()
            if (email.isBlank() or (!email.matches(Patterns.EMAIL_ADDRESS.toRegex()))) {
                etUsername.error = "check this field"
                etUsername.requestFocus()
                return@setOnClickListener
            }

            val password = etPassword.text.toString().trim()
            if (password.isBlank()) {
                etPassword.error = "check this field"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)

            val login = Login(1, "nHzszf21rhiD4plBqCYqwbdiUbNMwnJ4YtZDdnbP", "password", password, email)
            Api.loginRequest().login(login)
                    .enqueue(object : Callback<AccessToken> {
                        override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {

                            when (response.code()) {
                                200 -> {
                                    val sharedPreferences = getSharedPreferences("Shared_pref", Context.MODE_PRIVATE)

                                    sharedPreferences.edit {
                                        this.putString("access_token", response.body()!!.accessToken)
                                        this.putBoolean("isLoggedIn", true)
                                        this.apply()
                                    }

                                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))

                                }

                                else -> {
                                    Toast.makeText(this@MainActivity, "Check your credentials", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }

                        override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Unable to connect, check connection", Toast.LENGTH_SHORT).show()
                        }
                    })


        }
    }
}
