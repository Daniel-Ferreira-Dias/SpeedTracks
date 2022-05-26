package com.cme.speedtrackers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.FragmentEquipmentBinding



class EquipmentFragment : Fragment() {
    private lateinit var binding: FragmentEquipmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_equipment, container, false)
    }
}
