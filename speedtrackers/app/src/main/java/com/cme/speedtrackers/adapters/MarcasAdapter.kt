package com.cme.speedtrackers.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.MarcasActivity
import com.cme.speedtrackers.ModelosActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.GridLayoutMarcasItemBinding
import com.cme.speedtrackers.model.Marcas
import com.cme.speedtrackers.model.Modelos
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class MarcasAdapter : RecyclerView.Adapter<MarcasAdapter.HolderMarcas> {

    // context, get using construtor
    private var context: MarcasActivity

    //arraylist to hold pdfs
    private var marcasArrayList: ArrayList<Marcas>

    //viewbinding row_pdf_user.xml
    private lateinit var binding: GridLayoutMarcasItemBinding

    //firebase
    private val mAuth = FirebaseAuth.getInstance()

    //firebase database
    private lateinit var mDbRef: DatabaseReference


    // GlobalClass
    val compObj = GlobalClass.Companion


    // construtor
    constructor(context: MarcasActivity, marcasArrayList: ArrayList<Marcas>) {
        this.context = context
        this.marcasArrayList = marcasArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMarcas {
        // inflate layout
        binding = GridLayoutMarcasItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderMarcas(binding.root)
    }

    public fun setFilteredList(filteredList: java.util.ArrayList<Marcas>) {
        this.marcasArrayList = filteredList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HolderMarcas, position: Int) {
        // get data
        val model = marcasArrayList[position]
        val marcaId = model.ID
        val name = model.Nome
        val image = model.Imagem

        mDbRef = FirebaseDatabase.getInstance().getReference("Marcas")

        //set
        holder.textViewMarcas.text = name

        loadImage(model, holder)


        binding.selectBrand.setOnClickListener {
            compObj.Brand_Name = model.Nome
            compObj.Brand_ID = model.ID.toString()

            val bundle = Bundle()
            bundle.putString("marcaId", marcaId.toString())

            val intent = Intent(context, ModelosActivity::class.java)
            intent.putExtra("marcaId", marcaId.toString()) // Loads respective brand
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return marcasArrayList.size // number of records
    }

    inner class HolderMarcas(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewMarcas = binding.iconImageView
        val textViewMarcas = binding.titleTextView
    }


    private fun loadImage(model: Marcas, holder: HolderMarcas) {
        mDbRef.child(model.ID.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        Glide.with(context).load(model.Imagem)
                            .placeholder(R.drawable.progress_animation)
                            .into(holder.imageViewMarcas)
                    } catch (e: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}