package com.cme.speedtrackers.adapters

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.R
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.ActivityAddBinding
import com.cme.speedtrackers.databinding.BottomSheetShoesItemBinding
import com.cme.speedtrackers.dialogs.BottomSheetColorFragment
import com.cme.speedtrackers.dialogs.BottomSheetShoesFragment
import com.cme.speedtrackers.model.Modelos
import com.cme.speedtrackers.model.Shoes
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*
import java.lang.Exception
import java.util.ArrayList


class AddEquipmentToActivityAdapter(private var equipmentList: ArrayList<Shoes>) : RecyclerView.Adapter<AddEquipmentToActivityAdapter.CustomViewHolder>() {

    //Essential Variables
    private lateinit var binding: BottomSheetShoesItemBinding //View Binding
    private lateinit var context: Context //Context from Parent
    private lateinit var dbRef: DatabaseReference

    // GlobalClass
    val compObj = GlobalClass.Companion

    public fun setFilteredList(filteredList: ArrayList<Shoes>) {
        this.equipmentList = filteredList
        notifyDataSetChanged()
    }


    // Number of rows in display
    override fun getItemCount(): Int {
        return equipmentList.size
    }

    //Creating View with Binding and set Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        binding = BottomSheetShoesItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(binding.root)
    }

    // Overriding views with items values and deploy click listeners
    override fun onBindViewHolder(holder: AddEquipmentToActivityAdapter.CustomViewHolder, position: Int) {
        val currentView = equipmentList[position] // Current ViewItem

        // set Item to Value
        setModeloNameAndImage(currentView, holder)
        currentView.Shoe_Nome?.let { holder.tvName.setText(it) }
        loadImage(holder, currentView.ImageURL?.toString())

        //click Handler
        binding.currentCardView.setOnClickListener {
            compObj.activityShoe = currentView
            if (compObj.currentDialog.dialog!!.isShowing){
                compObj.currentDialog.dismiss()
            }
            println("Apertei - ${compObj.activityShoe.Model_Nome}")
        }

    }

    private fun setModeloNameAndImage(currentItem: Shoes, holder: AddEquipmentToActivityAdapter.CustomViewHolder) {

        loadImage(holder, currentItem.ImageURL.toString())
    }

    //Carregar uma imagem recebendo o URL
    private fun loadImage(holder: AddEquipmentToActivityAdapter.CustomViewHolder, imageURL: String) {
        try {
            Glide.with(context).load(imageURL)
                .placeholder(R.drawable.progress_animation)
                .into(holder.ivImagem)
        } catch (e: Exception) {
        }
    }

    // Set items Variables
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = binding.tvNome
        val ivImagem = binding.ivEquipment
    }
}
