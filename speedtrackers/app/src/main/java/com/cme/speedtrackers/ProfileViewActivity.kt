package com.cme.speedtrackers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cme.speedtrackers.databinding.ActivityProfileViewBinding
import com.cme.speedtrackers.model.Atividade
import com.example.bookapplicationv1.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private lateinit var binding: ActivityProfileViewBinding


class ProfileViewActivity : AppCompatActivity() {

    private lateinit var mUserDetails: User
    private var mSelectedImageUri: Uri? = null
    private var mUserProfileImageURL: String = ""
    private var userUID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        userUID = FirebaseAuth.getInstance().uid.toString()
        mUserDetails = User()
        getUserDetails()

        binding.btnEdit.setOnClickListener {
            //binding.btnEdit.setImageResource(R.drawable.edit_pressed)
            @Suppress("DEPRECATION")
            Handler().postDelayed(
                {
                    binding.btnSave.visibility = View.VISIBLE
                    binding.btnCancel.visibility = View.VISIBLE
                    binding.btnEdit.visibility = View.INVISIBLE
                    //binding.btnEdit.setImageResource(R.drawable.edit)
                },
                100
            )

            binding.etUsername.isEnabled = true
            binding.etFirstName.isEnabled = true
            binding.etLastName.isEnabled = true
            binding.etEmail.isEnabled = true
        }

        binding.btnCancel.setOnClickListener {

            @Suppress("DEPRECATION")
            Handler().postDelayed({
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnSave.visibility = View.INVISIBLE
                binding.btnCancel.visibility = View.INVISIBLE
            },
                100
            )
            binding.etUsername.isEnabled = false
            binding.etFirstName.isEnabled = false
            binding.etLastName.isEnabled = false
            binding.etEmail.isEnabled = false
        }
    }

    private fun getUserDetails(){
        var dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (atividadeSnap in snapshot.children){
                        val atividadeData = atividadeSnap.getValue(User::class.java)
                        if (atividadeData?.userUID == userUID){
                            mUserDetails = atividadeData
                            println("Cheguei Aqui")
                            println("${mUserDetails.userName} | ${mUserDetails.userUID}")
                            binding.etUsername.setText(mUserDetails.userName.toString())
                            binding.etEmail.setText(mUserDetails.userEmail.toString())
                            break
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setupActionBar(){

        setSupportActionBar(binding.toolbarSettingsActivity)

        val actionBar = supportActionBar

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white_24)
        }
        actionBar?.title = ""
        binding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }

        binding.btnSave.visibility = View.INVISIBLE
        binding.btnCancel.visibility = View.INVISIBLE
    }
}