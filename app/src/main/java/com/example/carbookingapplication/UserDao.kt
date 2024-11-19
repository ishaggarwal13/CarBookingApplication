package com.example.carbookingapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    // This method inserts a User object into the database
    @Insert
    suspend fun insert(user: User)

    // This method fetches a user by email and password asynchronously
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?
}