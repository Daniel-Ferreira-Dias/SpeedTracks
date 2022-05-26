package com.cme.speedtrackers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.FragmentActivityBinding


class ActivityFragment : Fragment() {
    private lateinit var binding: FragmentActivityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivityBinding.inflate(layoutInflater)

        //TYPE YOUR CODE



        return binding.root
    }
}