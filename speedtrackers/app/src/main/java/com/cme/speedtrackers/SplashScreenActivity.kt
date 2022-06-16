package com.cme.speedtrackers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.protobuf.Value

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var  firebaseAuth : FirebaseAuth

    private var id = ""
    val compObj = GlobalClass.Companion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()
        val auth = firebaseAuth.currentUser?.uid
        id = auth.toString()

        checkUserType()
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
            startActivity(Intent(this@SplashScreenActivity, BottomNavigationActivity::class.java))
            finish()
        }
    }

    private fun checkUserType(){
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(id)
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userType = "${snapshot.child("userType").value}"
                if (userType == "Admin"){
                    compObj.isAdmin = "Admin"
                }else{
                    compObj.isAdmin = "Normal Type"
                }
                Log.d("ADMIN", compObj.isAdmin.toString())
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}