package com.example.carbookingapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import android.Manifest
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var etPickup: EditText
    private lateinit var etDropOff: EditText
    private lateinit var etDate: EditText
    private lateinit var etPassengers: EditText
    private lateinit var btnSearchRide: Button
    private lateinit var selectedDate: String
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                fetchCurrentLocation(etPickup)
            } else {
                Toast.makeText(this, "Location permission not granted!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etPickup = findViewById(R.id.etPickup)
        etDropOff = findViewById(R.id.etDropOff)
        etDate = findViewById(R.id.etDate)
        etPassengers = findViewById(R.id.etPassengers)
        btnSearchRide = findViewById(R.id.btnSearchRide)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        etPickup.setOnClickListener {
            if (checkLocationPermission()) {
                fetchCurrentLocation(etPickup)
            } else {
                requestLocationPermission()
            }
        }

        etDropOff.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateAndFetchAddress(etDropOff)
        }

        etDate.setOnClickListener { showDatePicker() }

        btnSearchRide.setOnClickListener {
            if (validateInputs()) {
                val pickup = etPickup.text.toString()
                val dropOff = etDropOff.text.toString()
                val date = etDate.text.toString()
                val passengers = etPassengers.text.toString().toInt()

                val ride = Ride(pickup = pickup, dropOff = dropOff, date = date, passengers = passengers)


                lifecycleScope.launch(Dispatchers.IO) {
                    val db = Room.databaseBuilder(applicationContext, UserDatabase::class.java, "ride_db").build()
                    db.rideDao().insertRide(ride)
                }

                val intent = Intent(this, DriverListActivity::class.java).apply {
                    putExtra("PICKUP", pickup)
                    putExtra("DROPOFF", dropOff)
                    putExtra("DATE", date)
                    putExtra("PASSENGERS", passengers)
                }
                startActivity(intent)
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation(targetEditText: EditText) {
        if (checkLocationPermission()) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    try {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (!addresses.isNullOrEmpty()) {
                            targetEditText.setText(addresses[0].getAddressLine(0) ?: "")
                        } else {
                            Toast.makeText(this, "Unable to fetch current location!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: IOException) {
                        // Handle network or geocoder failure
                        e.printStackTrace()
                        Toast.makeText(this, "Failed to fetch address. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Location not available!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Location permission not granted!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateAndFetchAddress(targetEditText: EditText) {
        val location = targetEditText.text.toString()
        if (location.isNotEmpty()) {
            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocationName(location, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    targetEditText.setText(address.getAddressLine(0))
                } else {
                    Toast.makeText(this, "Invalid location!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                // Handle geocoder failure
                e.printStackTrace()
                Toast.makeText(this, "Failed to fetch address. Please check your network connection.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter a location to validate.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                etDate.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun validateInputs(): Boolean {
        return when {
            etPickup.text.isBlank() -> {
                etPickup.error = "Please enter a pickup location"
                false
            }
            etDropOff.text.isBlank() -> {
                etDropOff.error = "Please enter a drop-off location"
                false
            }
            etDate.text.isBlank() -> {
                etDate.error = "Please select a date"
                false
            }
            etPassengers.text.isBlank() || etPassengers.text.toString().toIntOrNull() == null -> {
                etPassengers.error = "Please enter a valid number of passengers"
                false
            }
            else -> true
        }
    }
}



