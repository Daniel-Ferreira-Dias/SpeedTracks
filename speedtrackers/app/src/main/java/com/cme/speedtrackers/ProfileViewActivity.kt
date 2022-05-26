package com.cme.speedtrackers

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cme.speedtrackers.databinding.ActivityProfileViewBinding
import com.example.bookapplicationv1.classes.User

private lateinit var binding: ActivityProfileViewBinding
private lateinit var mUserDetails: User

class ProfileViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        //binding.tvEdit.setOnClickListener(this)
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
        }
    }
}