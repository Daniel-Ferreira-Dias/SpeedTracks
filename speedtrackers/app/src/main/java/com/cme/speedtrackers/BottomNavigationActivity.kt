package com.cme.speedtrackers

import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.cme.speedtrackers.adapters.TabPageAdapter
import com.cme.speedtrackers.databinding.ActivityBottomNavigationBinding
import com.example.bookapplicationv1.classes.User
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private lateinit var binding: ActivityBottomNavigationBinding

class BottomNavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBar()
        var page: Int = intent.getIntExtra("Store", 0)
        binding.viewPager.currentItem = page

    }

    override fun onResume() {
        super.onResume()
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        getUserDetails()
    }

    private fun setUpBar() {
        val adapter = TabPageAdapter(this, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    public fun modificarPosicao(posicao : Int){
        binding.viewPager.currentItem = posicao
    }

    private fun getUserDetails(){
        var dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnap in snapshot.children){
                        val userData = userSnap.getValue(User::class.java)
                        if (userData?.userUID == FirebaseAuth.getInstance().uid){
                            compObj.currentUser = userData!!
                            println(compObj.currentUser.userName)
                            if (userData?.isAdmin == null){
                                var database = FirebaseDatabase.getInstance().getReference("Users")
                                database.child(FirebaseAuth.getInstance().uid.toString()).child("admin").setValue(false)
                            }
                            println("ADMIN: " + userData?.isAdmin + compObj.currentUser.isAdmin)
                            break
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}