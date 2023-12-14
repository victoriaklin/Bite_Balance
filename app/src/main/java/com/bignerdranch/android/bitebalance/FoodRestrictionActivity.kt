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

        val done = findViewById<Button>(R.id.done_button)
        done.setOnClickListener{
            val openHomePage = Intent(this, HomeActivity::class.java)
            startActivity(openHomePage)
        }
    }
}