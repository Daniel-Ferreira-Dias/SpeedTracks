package com.cme.speedtrackers

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.cme.speedtrackers.adapters.PasswordStrength
import com.cme.speedtrackers.databinding.ActivityRegisterBinding
import com.example.bookapplicationv1.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity(), TextWatcher {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var pass = ""
    private var confirmPass = ""
    private var userName = ""
    private var passwordconfirmed = false

    // database
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        if (binding.editPassword.text.toString().isEmpty()) {
            binding.passwordStrength.text = ""
            binding.progressBar.isVisible = false
        }


        //checkbox
        var isTicked = false

        binding.checkBox.setOnCheckedChangeListener { buttonview, isChecked ->
            isTicked = isChecked
        }

        // terms
        termAndConditions()

        // Already has an account
        binding.loginHereTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Register
        binding.registerButton.setOnClickListener {
            if (isTicked) {
                checkConditions()
            } else {
                Toast.makeText(
                    this,
                    "Tem que concordar com os termos e condição",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.editPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!passwordconfirmed && binding.editPassword.text.toString().isNotEmpty()) {
                    binding.editPassword.error = "Por favor escolha uma password mais segura"
                }

            }
            binding.progressBar.isVisible = true

        }

        binding.editPassword.addTextChangedListener(this)
    }

    // Terms
    private fun termAndConditions() {
        val spannableString = SpannableString("Declaro que li e aceito os termos e condições")

        val clickableSpan = object : ClickableSpan() {
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
        } else if (userName.isEmpty()) {
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
        val User = User(userName, email, userType, timestamp, userVerified, uid, userProfile, false)
        database.child(uid).setValue(User).addOnSuccessListener {
            Toast.makeText(this, "Conta criada com sucesso...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterActivity, OptionActivity::class.java))
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Conta criada sem sucesso...", Toast.LENGTH_SHORT).show()
        }

    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        updatePasswordStrengthView(s.toString())
    }

    override fun afterTextChanged(p0: Editable?) {}

    private fun updatePasswordStrengthView(password: String) {

        if (TextView.VISIBLE != binding.passwordStrength.visibility)
            return

        if (password == "") {
            binding.passwordStrength.text = ""
            binding.progressBar.progress = 0
            return
        }

        val str = PasswordStrength.calculateStrength(password)
        binding.passwordStrength.text = str.getText(this)
        binding.passwordStrength.setTextColor(str.color)
        passwordconfirmed = str.getText(this) != "Weak"

        binding.progressBar.progressDrawable.setColorFilter(
            str.color,
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        when {
            str.getText(this) == "Weak" -> {
                binding.progressBar.progress = 25
            }
            str.getText(this) == "Medium" -> {
                binding.progressBar.progress = 50
            }
            str.getText(this) == "Strong" -> {
                binding.progressBar.progress = 75
            }
            else -> {
                binding.progressBar.progress = 100
            }
        }
    }
}