package com.cme.speedtrackers

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import com.cme.speedtrackers.databinding.ActivityRegisterBinding
import com.example.bookapplicationv1.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var pass = ""
    private var confirmPass = ""
    private var userName = ""

    // database
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        //checkbox
        var isTicked = false

        binding.checkBox.setOnCheckedChangeListener { buttonview, isChecked ->
            isTicked = isChecked
        }

        // terms
        termAndConditions()

        // Already has an account
        binding.loginHereTextView.setOnClickListener {
            val intent = Intent(this, OptionActivity::class.java)
            startActivity(intent)
        }

        // Register
        binding.registerButton.setOnClickListener {
            if (isTicked){
                checkConditions()
            }
            else{
                Toast.makeText(this, "Tem que concordar com os termos e condição", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Terms
    private fun termAndConditions(){
        val spannableString = SpannableString("Declaro que li e aceito os termos e condições")

        val clickableSpan = object : ClickableSpan(){
            override fun onClick(widget: View) {
                Toast.makeText(this@RegisterActivity, "Incompleto...", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, 17, 45, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    // System checks if user has the right conditions

    private fun checkConditions() {
        userName = binding.editUserName.text.toString()
        email = binding.editTextEmail.text.toString()
        pass = binding.editPassword.text.toString()
        confirmPass = binding.editPasswordConfirm.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "Insira o seu email...", Toast.LENGTH_SHORT).show()
        }else if (userName.isEmpty()){
            Toast.makeText(this, "Insira o seu nome de utilizador...", Toast.LENGTH_SHORT).show()
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
                updateUserInfo()
            } else {
                Toast.makeText(
                    this,
                    "Algo correu mal: " + it.exception.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun updateUserInfo() {
        // Timestamp
        val timestamp = System.currentTimeMillis()

        // get current user uid
        val uid = firebaseAuth.uid.toString()

        //userType default
        val userType = "User" // Can be admin or user, if admin, will change on database manually

        //userDesc
        val userVerified = false

        //val profilepic
        val userProfile = ""

        database = FirebaseDatabase.getInstance().getReference("Users")
        val User = User(userName, email, userType, timestamp, userVerified, uid, userProfile)
        database.child(uid).setValue(User).addOnSuccessListener {
            Toast.makeText(this, "Conta criada com sucesso...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterActivity, OptionActivity::class.java))
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Conta criada sem sucesso...", Toast.LENGTH_SHORT).show()
        }

    }
}