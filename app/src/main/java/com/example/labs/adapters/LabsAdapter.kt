package com.example.labs.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labs.R
import com.example.labs.models.Lab
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.lab_item.view.*

class LabsAdapter(private val context: Context, private val labsList: MutableList<Lab>) : RecyclerView.Adapter<LabsAdapter.ViewHolder>() {
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

        holder.itemView.bookButton.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                    .setMessage("Book ${lab.name}?")
                    .setNegativeButton("NO") { dialogInterface, _ ->
                        //dismiss dialog
                        dialogInterface.dismiss()
                    }
                    .setPositiveButton("YES"){ dialogInterface, _ ->
                        //api calls go here
                        labsList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, labsList.size)
                        dialogInterface.dismiss()
                        dialogInterface.dismiss()
                    }.show()
        }
    }
}