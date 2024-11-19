package com.example.carbookingapplication

import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DriverDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_driver_details)

        val driverName = intent.getStringExtra("driverName") ?: "Unknown Driver"
        val price = intent.getStringExtra("price") ?: "N/A"
        val pickupTime = intent.getStringExtra("pickupTime") ?: "N/A"
        val dropTime = intent.getStringExtra("dropTime") ?: "N/A"
        val rideTime = intent.getStringExtra("rideTime") ?: "N/A"


        findViewById<TextView>(R.id.driverNameTextView).text = driverName
        findViewById<TextView>(R.id.priceTextView).text = "Price: $price"
        findViewById<TextView>(R.id.pickupTimeTextView).text = "Pickup Time: $pickupTime"
        findViewById<TextView>(R.id.dropTimeTextView).text = "Drop Time: $dropTime"
        findViewById<TextView>(R.id.rideTimeTextView).text = "Ride Duration: $rideTime"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        showRouteOnMap()
    }

    private fun showRouteOnMap() {
        val pickup = intent.getStringExtra("pickup_location") ?: ""
        val dropOff = intent.getStringExtra("drop_location") ?: ""

        if (pickup.isBlank() || dropOff.isBlank()) {
            Toast.makeText(this, "Invalid pickup or drop-off location", Toast.LENGTH_SHORT).show()
            return
        }

        val geocoder = Geocoder(this)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Use more specific location format (city, country)
                val pickupLatLng =
                    geocoder.getFromLocationName("$pickup, India", 1)?.firstOrNull()?.let {
                        LatLng(it.latitude, it.longitude)
                    }
                val dropLatLng =
                    geocoder.getFromLocationName("$dropOff, India", 1)?.firstOrNull()?.let {
                        LatLng(it.latitude, it.longitude)
                    }

                if (pickupLatLng != null && dropLatLng != null) {
                    withContext(Dispatchers.Main) {
                        googleMap.addMarker(MarkerOptions().position(pickupLatLng).title("Pickup"))
                        googleMap.addMarker(MarkerOptions().position(dropLatLng).title("Drop"))

                        val bounds = LatLngBounds.Builder()
                            .include(pickupLatLng)
                            .include(dropLatLng)
                            .build()

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

                        googleMap.addPolyline(
                            PolylineOptions()
                                .add(pickupLatLng, dropLatLng)
                                .width(5f)
                                .color(Color.BLUE)
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@DriverDetailsActivity,
                            "Unable to fetch locations",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DriverDetailsActivity,
                        "Error fetching locations: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}