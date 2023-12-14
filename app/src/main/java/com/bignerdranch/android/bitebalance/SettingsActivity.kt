package com.bignerdranch.android.bitebalance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val signOut = findViewById<Button>(R.id.signOutButton)
        signOut.setOnClickListener{
            val message = Toast.makeText(this, "You have been signed out", Toast.LENGTH_LONG)
            message.setGravity(Gravity.CENTER, 0, 0)
            message.show()
            val openMainPage = Intent(this, MainActivity::class.java)
            startActivity(openMainPage)
        }

        val back = findViewById<ImageView>(R.id.backButton)
        back.setOnClickListener{
            val openHomePage = Intent(this, HomeActivity::class.java)
            startActivity(openHomePage)
        }

        val change = findViewById<TextView>(R.id.change_dietary)
        change.setOnClickListener {
            val openDietary = Intent(this, FoodRestrictionActivity::class.java)
            startActivity(openDietary)
        }

    }
}