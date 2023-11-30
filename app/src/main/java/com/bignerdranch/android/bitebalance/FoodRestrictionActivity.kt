package com.bignerdranch.android.bitebalance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.Toast

class FoodRestrictionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_restriction)

        val createAccButton = findViewById<Button>(R.id.createAccount_button)
        createAccButton.setOnClickListener{
            val toast = Toast.makeText(this, "Welcome to Bite Balance!", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
            val openHomePage = Intent(this, HomeActivity::class.java)
            startActivity(openHomePage)
        }
    }
}