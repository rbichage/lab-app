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
import com.example.labs.models.BookLab
import com.example.labs.models.BookResource
import com.example.labs.models.Lab
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.lab_item.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LabsAdapter(private val context: Context, private val labsList: MutableList<Lab>, val previousContext: String) : RecyclerView.Adapter<LabsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.lab_item, parent, false))
    }

    override fun getItemCount(): Int = labsList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lab = labsList[position]

        holder.itemView.labName.text = "Name: ${lab.name}"
        holder.itemView.itemNumber.text = "${position+1}."
        holder.itemView.labCapacity.text = "Capacity: ${lab.capacity}"

        if (previousContext == "list"){
            holder.itemView.bookButton.visibility == View.INVISIBLE
        }

        holder.itemView.bookButton.setOnClickListener {

            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setMessage("Please wait...")

            val alertDialog = materialAlertDialogBuilder.show()

            val userId = context.getSharedPreferences("Shared_pref", Context.MODE_PRIVATE).getInt("user_id", 0)



            Api.nextRequest(context)
                    .bookLab(BookLab(lab.id, userId))
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            when (response.code()) {
                                200 -> {
                                    Toast.makeText(context, "Item Booked", Toast.LENGTH_SHORT).show()
                                    alertDialog.cancel()
                                    labsList.removeAt(position)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(position, labsList.size)
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
}