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
import com.cme.speedtrackers.ModelosActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.GridLayoutModelosItemBinding
import com.cme.speedtrackers.databinding.GridLayoutShoesBinding
import com.cme.speedtrackers.dialogs.BottomSheetColorFragment
import com.cme.speedtrackers.fragments.HomeFragment
import com.cme.speedtrackers.model.Marcas
import com.cme.speedtrackers.model.Modelos
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class ShoeAdapater : RecyclerView.Adapter<ShoeAdapater.HolderShoes> {

    // context, get using construtor
    private var context: Context

    //arraylist to hold pdfs
    private var shoesArrayList: ArrayList<Shoes>

    //viewbinding row_pdf_user.xml
    private lateinit var binding: GridLayoutShoesBinding

    //firebase
    private val mAuth = FirebaseAuth.getInstance()

    //firebase database
    private lateinit var mDbRef: DatabaseReference
    private lateinit var nDbRef: DatabaseReference

    // GlobalClass
    val compObj = GlobalClass.Companion

    // construtor
    constructor(context: Context, shoesArrayList: ArrayList<Shoes>) {
        this.context = context
        this.shoesArrayList = shoesArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderShoes {
        // inflate layout
        context = parent.context
        binding = GridLayoutShoesBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderShoes(binding.root)
    }

    override fun onBindViewHolder(holder: HolderShoes, position: Int) {
        // get data
        val model = shoesArrayList[position]
        val brand_id = model.Brand_ID




        mDbRef = FirebaseDatabase.getInstance().getReference("Sapatilhas")
        nDbRef = FirebaseDatabase.getInstance().getReference("Sapatilhas")

        loadShoe(model, holder)
        holder.textViewMarcas.text = model.Shoe_Nome
    }

    override fun getItemCount(): Int {
        return shoesArrayList.size // number of records
    }

    inner class HolderShoes(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewMarcas = binding.iconImageView
        val textViewMarcas = binding.titleTextView
        val kmsText = binding.tvDistacia
    }

    private fun loadShoe(model: Shoes, holder: ShoeAdapater.HolderShoes){
        nDbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    Glide.with(context).load(model.ImageURL)
                        .placeholder(R.drawable.progress_animation)
                        .into(holder.imageViewMarcas)
                } catch (e: Exception) {
                }
                holder.kmsText.text = model.KmTraveled.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}