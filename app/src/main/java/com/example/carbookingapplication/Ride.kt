package com.example.carbookingapplication


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rides_table")
data class Ride(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pickup: String,
    val dropOff: String,
    val date: String,
    val passengers: Int
)
