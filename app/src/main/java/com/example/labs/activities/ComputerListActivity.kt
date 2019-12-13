package com.example.labs.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labs.R
import com.example.labs.adapters.ComputerListAdapter
import com.example.labs.adapters.LabsAdapter
import com.example.labs.api.Api.Companion.nextRequest
import com.example.labs.models.Computer
import com.example.labs.models.Lab
import kotlinx.android.synthetic.main.activity_computer_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComputerListActivity : AppCompatActivity() {
    var computerList: MutableList<Computer> = ArrayList()
    private lateinit var resourceType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computer_list)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        resourceType = intent.getStringExtra("resourceType")
        toolbar.title = null

        when (resourceType) {
            "lab" -> {
                getLabs()
                toolbar.title = "Labs"
            }
            else -> {
                getLabResources()

            }
        }

    }

    private fun getLabs() {
        nextRequest(this)
                .getLabs()
                .enqueue(object : Callback<List<Lab>> {
                    override fun onResponse(call: Call<List<Lab>>, response: Response<List<Lab>>) {
                        val labs = response.body()!!.toMutableList()

                        val layoutManager = LinearLayoutManager(this@ComputerListActivity)
                        recycler.layoutManager = layoutManager
                        recycler.addItemDecoration(DividerItemDecoration(this@ComputerListActivity, layoutManager.orientation))
                        recycler.adapter = LabsAdapter(this@ComputerListActivity, labs, "booking")
                        recycler.hasFixedSize()

                    }

                    override fun onFailure(call: Call<List<Lab>>, t: Throwable) {
                        Toast.makeText(this@ComputerListActivity, "Unable to connect", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getLabResources() {
        when (resourceType) {
            "computer" -> {
                requestLbResources("computers")
            }

            "lcd" -> {
                requestLbResources("lcd")

            }

            "projector" -> {
                requestLbResources("projectors")

            }
        }

    }


    private fun requestLbResources(type: String) {
        nextRequest((this))
                .getComputers(type)
                .enqueue(object : Callback<List<Computer>> {
                    override fun onResponse(call: Call<List<Computer>>, response: Response<List<Computer>>) {
                        computerList = response.body()!!.toMutableList()

                        val layoutManager = LinearLayoutManager(this@ComputerListActivity)
                        recycler.layoutManager = layoutManager
                        recycler.addItemDecoration(DividerItemDecoration(this@ComputerListActivity, layoutManager.orientation))
                        recycler.hasFixedSize()

                        when (type) {
                            "computers" -> {
                                toolbar.title = "Computers"
                                recycler.adapter = ComputerListAdapter(this@ComputerListActivity, computerList, "")
                            }

                            "lcd" -> {
                                toolbar.title = "LCDs"
                                recycler.adapter = ComputerListAdapter(this@ComputerListActivity, computerList, "")
                            }

                            "projectors" -> {
                                toolbar.title = "Projectors"
                                recycler.adapter = ComputerListAdapter(this@ComputerListActivity, computerList, "")
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Computer>>, t: Throwable) {
                        Toast.makeText(this@ComputerListActivity, "Unable to connect", Toast.LENGTH_SHORT).show()
                    }
                })
    }
}