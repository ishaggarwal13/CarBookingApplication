package com.example.carbookingapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var dbHelper: UserDatabase
    private lateinit var loginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        nameEditText = findViewById(R.id.nameEditText)
        contactEditText = findViewById(R.id.contactEditText)
        ageEditText = findViewById(R.id.ageEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginTextView = findViewById(R.id.loginTextView)
        dbHelper = UserDatabase.getDatabase(this)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val contact = contactEditText.text.toString().trim()
            val age = ageEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validation
            if (name.isNotEmpty() && contact.isNotEmpty() && age.isNotEmpty() &&
                email.isNotEmpty() && password.isNotEmpty()
            ) {
                if (contact.length != 10 || !contact.all { it.isDigit() }) {
                    Toast.makeText(this, "Please enter a valid contact number", Toast.LENGTH_SHORT).show()
                } else if (!age.all { it.isDigit() } || age.toInt() <= 0) {
                    Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = User(
                        name = name,
                        contact = contact,
                        age = age.toInt(),
                        email = email,
                        password = password
                    )
                    lifecycleScope.launch {
                        dbHelper.userDao().insert(newUser)
                        Toast.makeText(this@RegisterActivity, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                        finish()  // Close the registration page and return to login
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}