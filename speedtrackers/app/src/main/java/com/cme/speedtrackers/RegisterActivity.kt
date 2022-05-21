package com.cme.speedtrackers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cme.speedtrackers.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var pass = ""
    private var confirmPass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Already has an account
        binding.loginHereTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Register
        binding.registerButton.setOnClickListener {
            checkConditions()
        }
    }

    // System checks if user has the right conditions

    private fun checkConditions() {
        email = binding.editTextEmail.text.toString()
        pass = binding.editPassword.text.toString()
        confirmPass = binding.editPasswordConfirm.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "Insira o seu email...", Toast.LENGTH_SHORT).show()
        } else if (pass.isEmpty()) {
            Toast.makeText(this, "Insira a sua password...", Toast.LENGTH_SHORT).show()
        } else if (confirmPass.isEmpty()) {
            Toast.makeText(this, "Insira a confirmação da password", Toast.LENGTH_SHORT).show()
        } else if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
            if (pass == confirmPass) {
                registerUser()
            } else {
                Toast.makeText(this, "As passwords não coincidem", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // System register user information

    private fun registerUser() {
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Utilizador registado!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Algo correu mal: " + it.exception.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}