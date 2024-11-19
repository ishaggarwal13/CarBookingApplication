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

class DriverAdapter(private val context: Context,
                    private val locationGroups: List<LocationGroup>,
                    private val pickup: String?,
                    private val dropOff: String?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_DRIVER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.item_location_header, parent, false)
            LocationViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.driver_card, parent, false)
            DriverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var itemPosition = position
        for (locationGroup in locationGroups) {
            // If it's the location view
            if (itemPosition == 0) {
                val locationViewHolder = holder as LocationViewHolder
                locationViewHolder.bind(locationGroup.location)
                return
            }
            itemPosition -= 1 // Adjust position to move past location header

            // If it's a driver view
            if (itemPosition < locationGroup.drivers.size) {
                val driverViewHolder = holder as DriverViewHolder
                driverViewHolder.bind(locationGroup.drivers[itemPosition])
                return
            }

            // Update itemPosition after binding all drivers for a location
            itemPosition -= locationGroup.drivers.size
        }
    }


    override fun getItemCount(): Int {
        var count = 0
        for (locationGroup in locationGroups) {
            count += 1 // for the location item
            count += locationGroup.drivers.size // for each driver under the location
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        var itemPosition = position
        for (locationGroup in locationGroups) {
            if (itemPosition == 0) return VIEW_TYPE_HEADER
            itemPosition -= 1

            if (itemPosition < locationGroup.drivers.size) return VIEW_TYPE_DRIVER
            itemPosition -= locationGroup.drivers.size
        }
        return -1
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

            // Intent for "Book" Button
            bookButton.setOnClickListener {
                val intent = Intent(itemView.context, BookingConfirmationActivity::class.java)
                intent.putExtra("driverName", driverData.driverName)
                intent.putExtra("price", driverData.price)
                intent.putExtra("pickupTime", driverData.pickupTime)
                intent.putExtra("dropTime", driverData.dropTime)
                intent.putExtra("location", driverData.location)
                intent.putExtra("rideTime", driverData.rideTime)
                itemView.context.startActivity(intent)
            }

            // Intent for clicking on the card
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DriverDetailsActivity::class.java)
                intent.putExtra("pickup_location", pickup)
                intent.putExtra("drop_location", dropOff)
                intent.putExtra("driverName", driverData.driverName)
                intent.putExtra("price", driverData.price)
                intent.putExtra("pickupTime", driverData.pickupTime)
                intent.putExtra("dropTime", driverData.dropTime)
                intent.putExtra("location", driverData.location)
                intent.putExtra("rideTime", driverData.rideTime)
                itemView.context.startActivity(intent)
            }
        }
    }
}