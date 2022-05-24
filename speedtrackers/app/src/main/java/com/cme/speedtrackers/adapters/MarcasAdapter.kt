package com.cme.speedtrackers.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.GridLayoutMarcasItemBinding
import com.cme.speedtrackers.model.Marcas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class MarcasAdapter : RecyclerView.Adapter<MarcasAdapter.HolderMarcas> {

    // context, get using construtor
    private var context: Context

    //arraylist to hold pdfs
    private var marcasArrayList: ArrayList<Marcas>

    //viewbinding row_pdf_user.xml
    private lateinit var binding: GridLayoutMarcasItemBinding

    //firebase
    private val mAuth = FirebaseAuth.getInstance()

    //firebase database
    private lateinit var mDbRef: DatabaseReference



    // construtor
    constructor(context: Context, marcasArrayList: ArrayList<Marcas>) {
        this.context = context
        this.marcasArrayList = marcasArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMarcas {
        // inflate layout
        binding = GridLayoutMarcasItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderMarcas(binding.root)
    }

    override fun onBindViewHolder(holder: HolderMarcas, position: Int) {
        // get data
        val model = marcasArrayList[position]
        val id = model.marcaId
        val name = model.marcaName
        val image = model.marcaImage

        mDbRef = FirebaseDatabase.getInstance().reference

        loadMarcas(model, holder)

        //set
        holder.textViewMarcas.text = name



    }

    override fun getItemCount(): Int {
        return marcasArrayList.size // number of records
    }

    inner class HolderMarcas(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewMarcas = binding.iconImageView
        var textViewMarcas = binding.titleTextView
    }


    // Load Information from Database
    private fun loadMarcas(model: Marcas, holder: HolderMarcas ){
        val ref = FirebaseDatabase.getInstance().getReference("Marcas")
        ref.child(model.marcaName)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile = "${snapshot.child("Imagem").value}"
                val name = "${snapshot.child("Nome").value}"

                // set image
                try {
                    Glide.with(context)
                        .load(profile)
                        .placeholder(R.drawable.ic_baseline_person_24_red)
                        .into(holder.imageViewMarcas)
                } catch (e: Exception) {
                    holder.imageViewMarcas.setImageResource(R.drawable.ic_baseline_person_24_red)
                }

                holder.textViewMarcas.text = name
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}