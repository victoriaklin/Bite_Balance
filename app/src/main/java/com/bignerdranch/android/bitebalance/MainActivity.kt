package com.bignerdranch.android.bitebalance

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

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

        val loginButton = findViewById<Button>(R.id.login_button)
        // if username and password match
        // show toast that says "Login successful!"
        // if username and password dont match
        // show toast that says "Incorrect username or password"
        loginButton.setOnClickListener{
            val openHomePage = Intent(this, HomeActivity::class.java)
            startActivity(openHomePage)
        }


        // Use the below for the other activities as well
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val account = GoogleSignIn.getLastSignedInAccount(this)

        val googleButton = findViewById<ImageView>(R.id.google_button)
        googleButton.setOnClickListener{signIn()}


    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_LONG).show()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }


}