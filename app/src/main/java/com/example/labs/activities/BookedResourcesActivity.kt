package com.example.labs.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.labs.R
import kotlinx.android.synthetic.main.activity_booked_resources.*

class BookedResourcesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_resources)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}