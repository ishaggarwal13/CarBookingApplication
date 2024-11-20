package com.example.carbookingapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DriverListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var driverAdapter: DriverAdapter
    private lateinit var locationGroups: MutableList<LocationGroup>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_driver_list)

        val pickup = intent.getStringExtra("pickup_location")
        val dropOff = intent.getStringExtra("drop_location")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        locationGroups = mutableListOf()

        driverAdapter = DriverAdapter(this, locationGroups, pickup, dropOff)
        recyclerView.adapter = driverAdapter

        loadDriverData()
    }

    private fun loadDriverData() {
        val driverList = mutableListOf(
            DriverData("Arun Verma", "10:00 AM", "12:20 PM", "Chandigarh", "140 min", "$420"),
            DriverData("Nitin Kumar", "11:00 AM", "13:00 PM", "Chandigarh", "120 min", "600"),
            DriverData("Ankush", "12:00 PM", "14:00 PM", "Amritsar", "120 min", "400"),
            DriverData("Dhanish Amar", "13:00 PM", "15:10 PM", "Amritsar", "130 min", "450")
        )

        val locationMap = mutableMapOf<String, MutableList<DriverData>>()

        for (driver in driverList) {
            locationMap.putIfAbsent(driver.location, mutableListOf())
            locationMap[driver.location]?.add(driver)
        }

        locationGroups.clear()

        locationMap.forEach { (location, drivers) ->
            locationGroups.add(LocationGroup(location, drivers))
            Log.d("DriverAdapter", "Location: $location, Drivers count: ${drivers.size}")
            drivers.forEach { driver ->
                Log.d("DriverAdapter", "Driver: ${driver.driverName}")
            }
        }

        driverAdapter.notifyDataSetChanged()
    }
}