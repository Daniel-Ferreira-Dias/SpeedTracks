package com.cme.speedtrackers.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cme.speedtrackers.R
import com.cme.speedtrackers.adapters.SettingsAdapter
import com.cme.speedtrackers.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        //TYPE CODE HERE
        binding.recycleViewSettings.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewSettings.adapter = SettingsAdapter()

        return binding.root
    }
}