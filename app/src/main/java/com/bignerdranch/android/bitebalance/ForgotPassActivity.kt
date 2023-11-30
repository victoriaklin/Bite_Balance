package com.bignerdranch.android.bitebalance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class ForgotPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        val sendEmailButton = findViewById<Button>(R.id.send_button)
        sendEmailButton.setOnClickListener {
            // if email is in database show toast saying "A link to reset your password has been sent."
            // if email not in database show toast saying "That email is not associated with an account"
        }
    }
}