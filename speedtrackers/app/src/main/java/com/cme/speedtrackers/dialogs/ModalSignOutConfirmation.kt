package com.cme.speedtrackers.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.cme.speedtrackers.AuthenticationActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.ModalSignoutConfirmationBinding
import com.google.firebase.auth.FirebaseAuth



class ModalSignOutConfirmation() : DialogFragment() {
    private lateinit var binding: ModalSignoutConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ModalSignoutConfirmationBinding.inflate(inflater, container, false)

        //TYPE YOUR CODE


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners(view: View) {
        binding.btnSave.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // User sign out
            var intent = Intent(context, AuthenticationActivity::class.java)
            context?.startActivity(intent)
            activity?.finish()
        }
        binding.ibGoBack.setOnClickListener { dismiss() }
        binding.btnCancel.setOnClickListener { dismiss() }
    }
}
