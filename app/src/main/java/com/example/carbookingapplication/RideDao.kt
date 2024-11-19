package com.example.carbookingapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RideDao {

    @Insert
    suspend fun insertRide(ride: Ride)

    @Query("SELECT * FROM rides_table")
    suspend fun getAllRides(): List<Ride>

    @Query("SELECT * FROM rides_table WHERE pickup = :pickupLocation")
    suspend fun getRidesByPickupLocation(pickupLocation: String): List<Ride>
}
