package com.cme.speedtrackers

import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.cme.speedtrackers.adapters.TabPageAdapter
import com.cme.speedtrackers.databinding.ActivityBottomNavigationBinding
import com.google.android.material.tabs.TabLayout

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
}