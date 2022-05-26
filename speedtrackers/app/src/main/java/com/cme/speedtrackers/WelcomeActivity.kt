package com.cme.speedtrackers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.cme.speedtrackers.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var botaoComecar: Button
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        botaoComecar = binding.welcomeButton
        botaoComecar.setOnClickListener {
            closeOpenActivity(outraActivity = AuthenticationActivity::class.java)

        }
    }


    private fun closeOpenActivity(outraActivity: Class<*>) {
        val x = Intent(this, outraActivity)
        finish()
        startActivity(x)
    }


}