package com.example.labs.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labs.R
import com.example.labs.models.Computer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.computer_item.view.*

class ComputerListAdapter(private val context: Context, private val computerList: MutableList<Computer>, private val resourceType: String) : RecyclerView.Adapter<ComputerListAdapter.ViewHolder>(){


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
        holder.itemView.itemNumber.text = "${position+1}."


        holder.itemView.bookButton.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                    .setMessage("Book ${computer.name}?")
                    .setNegativeButton("NO") { dialogInterface, _ ->
                        //dismiss dialog
                        dialogInterface.dismiss()
                    }
                    .setPositiveButton("YES"){ dialogInterface, _ ->
                        //api calls go here]

                        computerList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, computerList.size)
                        dialogInterface.dismiss()
                    }.show()
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}