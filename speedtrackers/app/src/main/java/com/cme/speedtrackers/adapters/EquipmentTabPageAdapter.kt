package com.cme.speedtrackers.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cme.speedtrackers.fragments.*


class EquipmentTabPageAdapter(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position)
        {
            0 -> EquipmentListFragment()
            1 -> EquipmentHistoryFragment()
            else -> EquipmentListFragment()
        }
    }
}