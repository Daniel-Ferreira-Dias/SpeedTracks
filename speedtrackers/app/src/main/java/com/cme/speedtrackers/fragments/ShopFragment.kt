package com.cme.speedtrackers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.FragmentShopBinding


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(layoutInflater)

        //TYPE YOUR CODE HERE

        return binding.root
    }
}