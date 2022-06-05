package com.cme.speedtrackers.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.cme.speedtrackers.MarcasActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.adapters.EquipmentTabPageAdapter
import com.cme.speedtrackers.adapters.SettingsAdapter
import com.cme.speedtrackers.adapters.TabPageAdapter
import com.cme.speedtrackers.databinding.FragmentEquipmentBinding
import com.google.android.material.tabs.TabLayout


class EquipmentFragment : Fragment() {
    private lateinit var binding: FragmentEquipmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEquipmentBinding.inflate(layoutInflater)

        //Type YOUR CODE HERE
        setUpBar()


        return binding.root
    }

    private fun setUpBar() {
        val adapter = EquipmentTabPageAdapter(this.requireActivity(), binding.tabLayout.tabCount)
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

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })

    }
}

