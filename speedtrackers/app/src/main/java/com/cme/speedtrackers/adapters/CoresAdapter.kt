package com.cme.speedtrackers.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.ModelosActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.ResumeShoeActivity
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.GridLayoutCoresItemBinding
import com.cme.speedtrackers.model.Cores
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CoresAdapter : RecyclerView.Adapter<CoresAdapter.HolderCores> {

    // context, get using construtor
    private var context: Context

    //arraylist to hold pdfs
    private var coresArrayList: ArrayList<Cores>

    //viewbinding row_pdf_user.xml
    private lateinit var binding: GridLayoutCoresItemBinding

    // GlobalClass
    val compObj = GlobalClass.Companion


    // construtor
    constructor(context: Context, coresArrayList: ArrayList<Cores>) {
        this.context = context
        this.coresArrayList = coresArrayList
    }

    public interface CallbackInterface {
        fun passResultCallback(message: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCores {
        // inflate layout
        binding = GridLayoutCoresItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCores(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCores, position: Int) {
        // get data
        val model = coresArrayList[position]
        val id = model.ID_Cor

        loadImage(model, holder)
        holder.imageViewMarcas.setOnClickListener {
            compObj.Color_ID = model.ID_Cor.toString()
            if (compObj.Color_ID == "") {
                Toast.makeText(context, "Tem que selecionar uma cor", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(context, ResumeShoeActivity::class.java)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return coresArrayList.size // number of records
    }

    inner class HolderCores(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewMarcas = binding.iconImageView
    }


    private fun loadImage(model: Cores, holder: HolderCores) {
        val ref = FirebaseDatabase.getInstance().getReference("Cores")
        ref.child(model.ID_Cor.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        Glide.with(context).load(model.Imagem_Cor)
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