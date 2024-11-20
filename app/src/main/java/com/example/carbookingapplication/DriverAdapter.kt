package com.example.carbookingapplication

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class DriverAdapter(
    private val context: Context,
    private val locationGroups: List<LocationGroup>,
    private val pickup: String?,
    private val dropOff: String?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_DRIVER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.item_location_header, parent, false)
            LocationViewHolder(view)
        } else {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.driver_card, parent, false)
            DriverViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return locationGroups.sumOf { it.drivers.size + 1 }
    }

    override fun getItemViewType(position: Int): Int {
        var currentPosition = position
        for (group in locationGroups) {
            if (currentPosition == 0) {
                return VIEW_TYPE_HEADER
            }
            currentPosition--
            if (currentPosition < group.drivers.size) {
                return VIEW_TYPE_DRIVER
            }
            currentPosition -= group.drivers.size
        }
        return VIEW_TYPE_DRIVER
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var currentPosition = position
        var groupIndex = 0

        while (groupIndex < locationGroups.size) {
            val currentGroup = locationGroups[groupIndex]

            if (currentPosition == 0) {
                (holder as LocationViewHolder).bind(currentGroup.location)
                return
            }
            currentPosition--

            if (currentPosition < currentGroup.drivers.size) {
                (holder as DriverViewHolder).bind(currentGroup.drivers[currentPosition])
                return
            }

            currentPosition -= currentGroup.drivers.size
            groupIndex++
        }
    }

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val locationTextView: TextView = itemView.findViewById(R.id.locationHeader)

        fun bind(location: String) {
            locationTextView.text = location
        }
    }

    inner class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val driverNameTextView: TextView = itemView.findViewById(R.id.driverName)
        private val pickupDetailsTextView: TextView = itemView.findViewById(R.id.pickupDetails)
        private val dropDetailsTextView: TextView = itemView.findViewById(R.id.dropDetails)
        private val locationDetailsTextView: TextView = itemView.findViewById(R.id.locationDetails)
        private val rideTimeTextView: TextView = itemView.findViewById(R.id.rideTime)
        private val driverPriceTextView: TextView = itemView.findViewById(R.id.driverPrice)
        private val bookButton: Button = itemView.findViewById(R.id.bookButton)

        fun bind(driverData: DriverData) {
            driverNameTextView.text = driverData.driverName
            pickupDetailsTextView.text = "Pickup: ${driverData.pickupTime}"
            dropDetailsTextView.text = "Drop: ${driverData.dropTime}"
            locationDetailsTextView.text = "Location: ${driverData.location}"
            rideTimeTextView.text = driverData.rideTime
            driverPriceTextView.text = "Price: ${driverData.price}"

            bookButton.setOnClickListener {
                val intent = Intent(itemView.context, BookingConfirmationActivity::class.java).apply {
                    putExtra("driverName", driverData.driverName)
                    putExtra("price", driverData.price)
                    putExtra("pickupTime", driverData.pickupTime)
                    putExtra("dropTime", driverData.dropTime)
                    putExtra("location", driverData.location)
                    putExtra("rideTime", driverData.rideTime)
                }
                itemView.context.startActivity(intent)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DriverDetailsActivity::class.java).apply {
                    putExtra("pickup_location", pickup)
                    putExtra("drop_location", dropOff)
                    putExtra("driverName", driverData.driverName)
                    putExtra("price", driverData.price)
                    putExtra("pickupTime", driverData.pickupTime)
                    putExtra("dropTime", driverData.dropTime)
                    putExtra("location", driverData.location)
                    putExtra("rideTime", driverData.rideTime)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}