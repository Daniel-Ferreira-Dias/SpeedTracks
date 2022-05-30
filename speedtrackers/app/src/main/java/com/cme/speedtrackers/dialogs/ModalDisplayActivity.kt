package com.cme.speedtrackers.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.FragmentModalDisplayActivityBinding


class ModalDisplayActivity : DialogFragment() {
    private lateinit var binding: FragmentModalDisplayActivityBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentModalDisplayActivityBinding.inflate(inflater, container, false)

        //TYPE YOUR CODE



        return binding.root
    }
}