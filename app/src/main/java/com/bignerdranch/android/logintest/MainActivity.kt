package com.bignerdranch.android.logintest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newAccountButton = findViewById<Button>(R.id.noaccount_button)
        newAccountButton.setOnClickListener{
            val openSignUpPage = Intent(this, SignUpActivity::class.java)
            startActivity(openSignUpPage)
        }

        val forgotPassButton = findViewById<TextView>(R.id.forgotpass)
        forgotPassButton.setOnClickListener {
            val openForgotPage = Intent(this, ForgotPassActivity::class.java)
            startActivity(openForgotPage)
        }
    }



}