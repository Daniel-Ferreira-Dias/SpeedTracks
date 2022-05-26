package com.cme.speedtrackers.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.GridLayoutMarcasItemBinding
import com.cme.speedtrackers.databinding.GridLayoutModelosItemBinding
import com.cme.speedtrackers.model.Marcas
import com.cme.speedtrackers.model.Modelos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class ModelosAdapter : RecyclerView.Adapter<ModelosAdapter.HolderModelos> {

    // context, get using construtor
    private var context: Context

    //arraylist to hold pdfs
    private var modelosArrayList: ArrayList<Modelos>

    //viewbinding row_pdf_user.xml
    private lateinit var binding: GridLayoutModelosItemBinding

    //firebase
    private val mAuth = FirebaseAuth.getInstance()

    //firebase database
    private lateinit var mDbRef: DatabaseReference


    // construtor
    constructor(context: Context, modelosArrayList: ArrayList<Modelos>) {
        this.context = context
        this.modelosArrayList = modelosArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderModelos {
        // inflate layout
        binding = GridLayoutModelosItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderModelos(binding.root)
    }

    override fun onBindViewHolder(holder: HolderModelos, position: Int) {
        // get data
        val model = modelosArrayList[position]
        val nome = model.Nome_Modelo

        mDbRef = FirebaseDatabase.getInstance().getReference("Marcas")

        //set
        holder.textViewMarcas.text = nome

        loadImage(model, holder)

        // Handle Click
        binding.selectModel.setOnClickListener {
            Toast.makeText( context, "Selecionaste o modelo ${nome}!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return modelosArrayList.size // number of records
    }

    inner class HolderModelos(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewMarcas = binding.iconImageView
        val textViewMarcas = binding.titleTextView
    }


    private fun loadImage(model: Modelos, holder: HolderModelos) {
        mDbRef.child(model.ID_Modelo.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        Glide.with(context).load(model.Imagem_Modelo)
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