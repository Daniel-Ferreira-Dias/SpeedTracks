package com.cme.speedtrackers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cme.speedtrackers.databinding.ActivityOptionBinding
import com.google.firebase.auth.FirebaseAuth

class OptionActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityOptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addShoe.setOnClickListener{
            val intent = Intent(this, MarcasActivity::class.java)
            startActivity(intent)
        }
        binding.goToStore.setOnClickListener {
            val intent = Intent(this, BottomNavigationActivity::class.java)
            intent.putExtra("Store", 1)
            startActivity(intent)
        }
    }
}