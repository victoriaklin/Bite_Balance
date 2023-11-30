package com.bignerdranch.android.bitebalance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FoodRestrictionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_restriction)

        val createAccButton = findViewById<Button>(R.id.createAccount_button)
        createAccButton.setOnClickListener{
            // toast that says "Welcome to Bite Balance!"
            val openHomePage = Intent(this, HomeActivity::class.java)
            startActivity(openHomePage)
        }
    }
}