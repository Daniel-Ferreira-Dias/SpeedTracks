package com.cme.speedtrackers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.cme.speedtrackers.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    lateinit var botaoLogin: Button
    lateinit var botaoRegister: Button

    private lateinit var binding: ActivityAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        botaoLogin = binding.loginButton
        botaoLogin.setOnClickListener{
            executarOutraActivity(outraActivity = LoginActivity::class.java)

        }
        botaoRegister = binding.registerButton
        botaoRegister.setOnClickListener{
            executarOutraActivity(outraActivity = RegisterActivity::class.java)
        }
    }

    private fun executarOutraActivity(outraActivity: Class<*>) {
        val x = Intent(this, outraActivity)
        startActivity(x)
    }
}