package com.example.carbookingapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Locale

class BookingConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking_confirmation)

        val driverName = intent.getStringExtra("driverName") ?: "Unknown Driver"
        val price = intent.getStringExtra("price") ?: "N/A"
        val pickupTime = intent.getStringExtra("pickupTime") ?: "N/A"
        val dropTime = intent.getStringExtra("dropTime") ?: "N/A"
        val rideTime = intent.getStringExtra("rideTime") ?: "N/A"
        val location = intent.getStringExtra("location")

        val confirmationMessage = """
            You're off to your $location!
            Driver: $driverName
            Price: $${price}
            Pickup Time: $pickupTime
            Drop Time: $dropTime
            Ride Time: $rideTime
        """.trimIndent()

        val bookingDetails = findViewById<TextView>(R.id.bookingDetails)
        bookingDetails.text = confirmationMessage

        val closeButton = findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            finish()
        }
    }
}
