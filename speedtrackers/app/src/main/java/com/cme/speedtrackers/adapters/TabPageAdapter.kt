package com.cme.speedtrackers.adapters

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cme.speedtrackers.fragments.*

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position)
        {
            0 -> HomeFragment()
            1 -> ShopFragment()
            2 -> EquipmentFragment()
            3 -> ActivityFragment()
            4 -> SettingsFragment()
            else -> HomeFragment()
        }
    }
}