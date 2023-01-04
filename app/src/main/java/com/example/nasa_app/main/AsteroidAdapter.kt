package com.example.nasa_app.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa_app.R
import com.example.nasa_app.database.NasaData

class AsteroidAdapter: RecyclerView.Adapter<ConstraintLayoutViewHolder>() {
    var data : List<NasaData> = listOf()

    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun getItemCount() = data.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstraintLayoutViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.nasa_records_card_view, parent, false)
        return ConstraintLayoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConstraintLayoutViewHolder, position: Int) {
        val item = data[position]
        holder.nasaRecordsCardView.setOnClickListener {
            it.findNavController().navigate(MainFragmentDirections.actionShowDetail(item))
        }

        holder.closeApproachDateTextView.text = item.closeApproachDate
        holder.codeNameTextView.text = item.codename
        holder.isHazardImageView.setImageResource(if (item.isPotentiallyHazardous) R.drawable.ic_status_potentially_hazardous else R.drawable.ic_status_normal)
        holder.isHazardImageView.contentDescription = if (item.isPotentiallyHazardous) "It's potential hazard" else "It's considered safe on earth"

    }




}


class ConstraintLayoutViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){
    var nasaRecordsCardView: ConstraintLayout = itemView.findViewById(R.id.nasaRecordsCardView)
    var closeApproachDateTextView: TextView = itemView.findViewById(R.id.closeApproachDateTextView)
    var codeNameTextView: TextView = itemView.findViewById(R.id.codeNameTextView)
    var isHazardImageView: ImageView = itemView.findViewById(R.id.isHazardImageView)
}