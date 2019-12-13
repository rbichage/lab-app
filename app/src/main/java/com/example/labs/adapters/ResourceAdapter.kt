package com.example.labs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labs.R
import com.example.labs.models.Resource
import kotlinx.android.synthetic.main.computer_item.view.*

class ResourceAdapter(val resourceList: List<Resource>): RecyclerView.Adapter<ResourceAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.computer_item, parent, false))
    }

    override fun getItemCount(): Int = resourceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resource = resourceList[position]
        holder.itemView.computerName.text = "Name: ${resource.resource}"
        holder.itemView.computerModel.text = "Booked By: ${resource.userName}"
        holder.itemView.serialNo.text = "Booked On. ${resource.dateBooked}"
        holder.itemView.itemNumber.text = "${position + 1}."

        holder.itemView.bookButton.visibility = View.INVISIBLE

    }
}