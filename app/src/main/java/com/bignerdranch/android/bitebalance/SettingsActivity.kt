package com.bignerdranch.android.bitebalance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.changeRestrictions -> {
                // Handle change dietary restrictions click
                true
            }
            R.id.signOut -> {
                // Handle sign out click
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}