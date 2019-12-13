package com.example.labs.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import com.example.labs.R
import com.example.labs.api.Api
import com.example.labs.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Response

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bComputers.setOnClickListener(this)
        bLCD.setOnClickListener(this)
        bLabs.setOnClickListener(this)
        bProjectors.setOnClickListener(this)

        setSupportActionBar(toolbar)

//        imageButton.setOnClickListener{
//            getSharedPreferences("Shared_pref", Context.MODE_PRIVATE).edit().clear().apply()
//
//            val intent = Intent(this, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        }

        checkUserType()
    }

    private fun checkUserType() {
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
        materialAlertDialogBuilder.setMessage("Please wait....")
        materialAlertDialogBuilder.setCancelable(false)

        val alertDialog = materialAlertDialogBuilder.show()

        Api.nextRequest(this)
                .getUser()
                .enqueue(object : retrofit2.Callback<User>{
                    override fun onResponse(call: Call<User>, response: Response<User>) {

                        when(response.code()){
                            200 -> {
                                alertDialog.dismiss()
                                val userType = response.body()!!.userTypeId
                                if (userType == 4){
                                    bComputers.visibility = View.VISIBLE
                                    bLCD.visibility = View.GONE
                                    bLabs.visibility = View.GONE
                                    bProjectors.visibility = View.GONE
                                }

                                else {
                                    bComputers.visibility = View.VISIBLE
                                    bLCD.visibility = View.VISIBLE
                                    bLabs.visibility = View.VISIBLE
                                    bProjectors.visibility = View.VISIBLE
                                }

                                getSharedPreferences("Shared_pref", Context.MODE_PRIVATE).edit {
                                    this.putInt("user_id", response.body()!!.id)
                                    this.apply()
                                }
                            }

                            else -> {
                                Toast.makeText(this@HomeActivity, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                                finishAndRemoveTask()
                            }
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@HomeActivity, "Unable to connect", Toast.LENGTH_SHORT).show()
                        alertDialog.dismiss()
                        finishAndRemoveTask()
                    }
                })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.menu_booked_resources -> {
                startActivity(Intent(this, BookedResourcesActivity::class.java))
            }

            R.id.menu_logout -> {
                getSharedPreferences("Shared_pref", Context.MODE_PRIVATE).edit().clear().apply()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onClick(p0: View?) {
        val intent = Intent(this, ComputerListActivity::class.java)
        var resourceType: String? = null


        when (p0?.id) {
            R.id.bComputers -> resourceType = "computer"
            R.id.bLCD -> resourceType = "lcd"
            R.id.bLabs -> resourceType = "lab"
            R.id.bProjectors -> resourceType = "projector"

        }

        intent.putExtra("resourceType", resourceType)
        startActivity(intent)
    }
}