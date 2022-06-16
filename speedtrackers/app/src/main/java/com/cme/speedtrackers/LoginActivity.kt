package com.cme.speedtrackers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cme.speedtrackers.databinding.ActivityLogin2Binding
import com.cme.speedtrackers.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private var email = ""
    private var pass = ""

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLogin2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Redirect to Register
        binding.registHereTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Login button
        binding.loginButton.setOnClickListener {
            checkConditions()
        }
    }

    // System checks if user has the right conditions

    private fun checkConditions() {
        email = binding.editTextEmail.text.toString()
        pass = binding.editTextPassword.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "Insira o seu email...", Toast.LENGTH_SHORT).show()
        } else if (pass.isEmpty()) {
            Toast.makeText(this, "Insira a sua password...", Toast.LENGTH_SHORT).show()
        } else if (email.isNotEmpty() && pass.isNotEmpty()){
            userLogin()
        }
    }

    private fun checkUserType(){
        val ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.uid!!)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userType = "${snapshot.child("userType").value}"
                if (userType == "Admin"){
                    compObj.isAdmin = "Admin"
                }else{
                    compObj.isAdmin = "Normal Type"
                }
                Log.d("ADMIN", compObj.isAdmin.toString())
                Toast.makeText(this@LoginActivity, "Login com sucesso !", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, BottomNavigationActivity::class.java)
                startActivity(intent)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // System login user
    private fun userLogin(){
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful){
                checkUserType()
            }else{
                Toast.makeText(this, "Autenticação sem sucesso:" + it.exception.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }
}