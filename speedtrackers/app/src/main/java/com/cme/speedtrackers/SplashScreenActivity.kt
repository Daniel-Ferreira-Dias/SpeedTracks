package com.cme.speedtrackers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.cme.speedtrackers.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var  firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            checkUser()
        }, 2000)

    }

    private fun checkUser(){
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            // user not logged
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else{
            startActivity(Intent(this, BottomNavigationActivity::class.java))
            finish()
        }
    }
}