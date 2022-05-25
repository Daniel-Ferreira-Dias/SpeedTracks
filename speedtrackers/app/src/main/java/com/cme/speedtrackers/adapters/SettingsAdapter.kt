package com.cme.speedtrackers.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.LoginActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.classes.SettingsClass
import com.cme.speedtrackers.databinding.ItemSettingsBinding
import java.util.ArrayList
import com.google.firebase.auth.FirebaseAuth

class SettingsAdapter() : RecyclerView.Adapter<SettingsAdapter.CustomViewHolder>() {

    //Essential Variables
    private lateinit var binding: ItemSettingsBinding //View Binding
    private val listOfSettings = addItemsToList() // List of Settings Items
    private lateinit var context: Context //Context from Parent


    // Number of rows in display
    override fun getItemCount(): Int {
        return listOfSettings.size
    }

    //Creating View with Binding and set Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        binding = ItemSettingsBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(binding.root)
    }

    // Overriding views with items values and deploy click listeners
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentView = listOfSettings.get(position) // Current ViewItem

        // set Item to Value
        currentView.name?.let { holder.tvText.setText(it) }
        currentView.icon?.let { holder.ivIcon.setImageResource(it) }

        // set ClickListener
        binding.btnAction.setOnClickListener {
            if (currentView.id == 7){
                FirebaseAuth.getInstance().signOut() // User sign out
                deployIntent() // Redirect to Login Activity
            }
        }
    }

    // Set items Variables
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvText = binding.tvEditMyProfile
        val ivIcon = binding.ivEditMyProfile
    }

    // Intent to Login Activity
    private fun deployIntent(){
        var intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
}

// Create list of Settings Items
private fun addItemsToList(): ArrayList<SettingsClass> {
    val settingsList = ArrayList<SettingsClass>()
    settingsList.add(SettingsClass(1, R.string.edit_my_profile, R.drawable.settings))
    settingsList.add(SettingsClass(2, R.string.change_password, R.drawable.key))
    settingsList.add(SettingsClass(3, R.string.notifications, R.drawable.bell))
    settingsList.add(SettingsClass(4, R.string.contact_us, R.drawable.email))
    settingsList.add(SettingsClass(5, R.string.terms_conditions, R.drawable.clipboard))
    settingsList.add(SettingsClass(6, R.string.privacy_policy, R.drawable.verified))
    settingsList.add(SettingsClass(7, R.string.sign_out, R.drawable.logout))

    return settingsList
}





