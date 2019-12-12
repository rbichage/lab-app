package com.example.labs.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labs.R
import com.example.labs.adapters.ComputerListAdapter
import com.example.labs.adapters.LabsAdapter
import com.example.labs.api.Api
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
        Api.nextRequest(this)
                .getLabs()
                .enqueue(object : Callback<List<Lab>> {
                    override fun onResponse(call: Call<List<Lab>>, response: Response<List<Lab>>) {
                        val labs = response.body()!!.toMutableList()

                        val layoutManager = LinearLayoutManager(this@ComputerListActivity)
                        recycler.layoutManager = layoutManager
                        recycler.addItemDecoration(DividerItemDecoration(this@ComputerListActivity, layoutManager.orientation))
                        recycler.adapter = LabsAdapter(this@ComputerListActivity, labs)
                        recycler.hasFixedSize()

                    }

                    override fun onFailure(call: Call<List<Lab>>, t: Throwable) {
                        Toast.makeText(this@ComputerListActivity, "Unable to connect", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun getLabResources() {
        Api.nextRequest((this))
                .getComputers()
                .enqueue(object : Callback<List<Computer>> {
                    override fun onResponse(call: Call<List<Computer>>, response: Response<List<Computer>>) {
                        computerList = response.body()!!.toMutableList()

                        val computersOnly = computerList.filter { it.resourceType == "Computer" }
                        val projectorsOnly = computerList.filter { it.resourceType == "Projector" }
                        Log.e("PROJECTOR", projectorsOnly.size.toString())
                        val lcdOnly = computerList.filter { it.resourceType == "Smart Board" }
                        val labsOnly = computerList.filter { it.resourceType == "lab" }

                        val layoutManager = LinearLayoutManager(this@ComputerListActivity)
                        recycler.layoutManager = layoutManager
                        recycler.addItemDecoration(DividerItemDecoration(this@ComputerListActivity, layoutManager.orientation))
                        recycler.hasFixedSize()

                        when (resourceType) {
                            "computer" -> {
                                toolbar.title = "Computers"

                                recycler.adapter = ComputerListAdapter(this@ComputerListActivity, computersOnly.toMutableList(), "computer")
                            }

                            "lcd" -> {
                                toolbar.title = "LCDs"
                                recycler.adapter = ComputerListAdapter(this@ComputerListActivity, lcdOnly.toMutableList(), "lcd")
                            }

                            "projector" -> {
                                toolbar.title = "Projectors"

                                recycler.adapter = ComputerListAdapter(this@ComputerListActivity, projectorsOnly.toMutableList(), "projector")
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<Computer>>, t: Throwable) {
                        Toast.makeText(this@ComputerListActivity, "Unable to connect", Toast.LENGTH_SHORT).show()
                    }
                })
    }
}