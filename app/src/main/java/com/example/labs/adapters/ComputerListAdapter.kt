package com.example.labs.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.labs.R
import com.example.labs.api.Api
import com.example.labs.models.BookResource
import com.example.labs.models.Computer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.computer_item.view.*
import kotlinx.android.synthetic.main.computer_item.view.bookButton
import kotlinx.android.synthetic.main.computer_item.view.itemNumber
import kotlinx.android.synthetic.main.lab_item.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComputerListAdapter(private val context: Context, private val computerList: MutableList<Computer>, val previousContext: String) : RecyclerView.Adapter<ComputerListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.computer_item, parent, false))
    }

    override fun getItemCount(): Int = computerList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val computer = computerList[position]
        holder.itemView.computerName.text = "Name: ${computer.name}"
        holder.itemView.computerModel.text = "Model: ${computer.model}"
        holder.itemView.serialNo.text = "Serial No. ${computer.serialNo}"
        holder.itemView.itemNumber.text = "${position + 1}."




        holder.itemView.bookButton.setOnClickListener {

            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setMessage("Please wait...")

            val alertDialog = materialAlertDialogBuilder.show()

            val userId = context.getSharedPreferences("Shared_pref", Context.MODE_PRIVATE).getInt("user_id", 0)

            Api.nextRequest(context)
                    .bookResource(BookResource(computer.id, userId))
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            when (response.code()) {
                                200 -> {
                                    Toast.makeText(context, "Item Booked", Toast.LENGTH_SHORT).show()
                                    alertDialog.cancel()
                                    computerList.removeAt(position)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, computerList.size)
                                }

                                else -> {
                                    Toast.makeText(context, "Oops, try again later", Toast.LENGTH_SHORT).show()
                                    alertDialog.dismiss()
                                }
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(context, "Check your connection", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        }
                    })
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}