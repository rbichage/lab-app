package com.example.labs.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labs.R
import com.example.labs.adapters.ComputerListAdapter
import com.example.labs.adapters.LabsAdapter
import com.example.labs.adapters.ResourceAdapter
import com.example.labs.api.Api
import com.example.labs.models.Computer
import com.example.labs.models.Lab
import com.example.labs.models.Resource
import kotlinx.android.synthetic.main.activity_booked_resources.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookedResourcesActivity : AppCompatActivity() {
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_resources)

        userId = getSharedPreferences("Shared_pref", Context.MODE_PRIVATE).getInt("user_id", 0)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        if (computersChip.isChecked) {
            getBookedResources()
        }

        labsChip.setOnClickListener { getLabs() }
        computersChip.setOnClickListener { getBookedResources()}

        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        recycler.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

    }

    private fun getBookedResources() {
        Api.nextRequest(this)
                .getBookedResources(userId)
                .enqueue(object : Callback<List<Resource>> {
                    override fun onResponse(call: Call<List<Resource>>, response: Response<List<Resource>>) {
                        when (response.code()) {
                            200 -> {
                                recycler.adapter = ResourceAdapter(response.body()!!)

                                if (response.body().isNullOrEmpty()) {
                                    Toast.makeText(this@BookedResourcesActivity, "You haven't made any booking", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Resource>>, t: Throwable) {
                        Toast.makeText(this@BookedResourcesActivity, "Unable to connect", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getLabs() {
        Api.nextRequest(this)
                .getBookedLabs(userId)
                .enqueue(object : Callback<List<Lab>> {
                    override fun onResponse(call: Call<List<Lab>>, response: Response<List<Lab>>) {
                        when (response.code()) {
                            200 -> {
                                recycler.adapter = LabsAdapter(this@BookedResourcesActivity, response.body()!!.toMutableList(), "list")

                                if (response.body().isNullOrEmpty()) {
                                    Toast.makeText(this@BookedResourcesActivity, "You haven't made any booking", Toast.LENGTH_SHORT).show()
                                }
                            }

                            else -> {
                                Toast.makeText(this@BookedResourcesActivity, "Oops, try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Lab>>, t: Throwable) {
                        Toast.makeText(this@BookedResourcesActivity, "Unable to connect", Toast.LENGTH_SHORT).show()
                    }
                })
    }
}