package com.cme.speedtrackers.adapters

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.*
import com.cme.speedtrackers.R
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.GridLayoutModelosItemBinding
import com.cme.speedtrackers.databinding.GridLayoutShoesBinding
import com.cme.speedtrackers.databinding.GridLayoutShopBinding
import com.cme.speedtrackers.dialogs.BottomSheetColorFragment
import com.cme.speedtrackers.dialogs.ModalSimpleShoeView
import com.cme.speedtrackers.fragments.HomeFragment
import com.cme.speedtrackers.model.Marcas
import com.cme.speedtrackers.model.Modelos
import com.cme.speedtrackers.model.Shoes
import com.cme.speedtrackers.model.Shop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class ShopAdapter : RecyclerView.Adapter<ShopAdapter.HolderShop> {

    // context, get using construtor
    private var context: Context

    //arraylist to hold pdfs
    private var shopArrayList: ArrayList<Shop>

    //viewbinding row_pdf_user.xml
    private lateinit var binding: GridLayoutShopBinding

    //firebase
    private val mAuth = FirebaseAuth.getInstance()

    //firebase database
    private lateinit var mDbRef: DatabaseReference

    //final rate
    //rating
    private var totalRating = 0
    private var currentRating = 0F
    private var finalrating = 0F

    // GlobalClass
    val compObj = GlobalClass.Companion

    // construtor
    constructor(context: Context, shopArrayList: ArrayList<Shop>) {
        this.context = context
        this.shopArrayList = shopArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderShop {
        // inflate layout
        context = parent.context
        binding = GridLayoutShopBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderShop(binding.root)
    }

    override fun onBindViewHolder(holder: HolderShop, position: Int) {
        // get data
        val model = shopArrayList[position]
        val price = model.Preço

        if (model.Stock < 10) {
            binding.tvWarningStock.visibility = View.VISIBLE
        } else {
            binding.tvWarningStock.visibility = View.GONE
        }

        binding.rlClick.setOnClickListener {
            val intent = Intent(context, ShoeFromShopDetailActivity::class.java)
            intent.putExtra("List", model.ID_Modelo.toString())
            intent.putExtra("Marca", model.ID_Marca.toString())
            context.startActivity(intent)
        }


        mDbRef = FirebaseDatabase.getInstance().getReference("Shop").child("Modelos")
        loadRating(model, holder)
        holder.rating.rating = finalrating
        loadShoe(model, holder)

        binding.tvPrice.text = model.Preço.toString()
        binding.titleTextView.text = model.Nome_Modelo
    }

    override fun getItemCount(): Int {
        return shopArrayList.size // number of records
    }

    inner class HolderShop(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewModelo = binding.iconImageView
        val textViewMarcas = binding.titleTextView
        val marcaView = binding.iconBrandView
        val preço = binding.tvPrice
        val warning_tv = binding.tvWarningStock
        val rating = binding.userRating
    }

    public fun setFilteredList(filteredList: java.util.ArrayList<Shop>) {
        this.shopArrayList = filteredList
        Log.e("DATACHANGE", "${shopArrayList.size}, ${shopArrayList[0].ID_Modelo}")
        notifyDataSetChanged()
    }

    private fun loadShoe(model: Shop, holder: ShopAdapter.HolderShop) {
        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    Glide.with(context).load(model.Imagem_Modelo)
                        .placeholder(R.drawable.progress_animation)
                        .into(holder.imageViewModelo)
                } catch (e: Exception) {
                }

                try {
                    Glide.with(context).load(model.Imagem_Marca)
                        .placeholder(R.drawable.progress_animation)
                        .into(holder.marcaView)
                } catch (e: Exception) {
                }
                binding.tvPrice.text = model.Preço.toString()
                Log.d("Preço", model.Preço.toString())
                binding.titleTextView.text = model.Nome_Modelo
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun loadRating(model: Shop, holder: ShopAdapter.HolderShop) {
        val mref = FirebaseDatabase.getInstance().getReference("Marcas").child(model.ID_Marca.toString()).child("Modelos")
        mref.child(model.ID_Modelo.toString()).child("Reviews").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val booktotalrating = "${snapshot.child("totalreviews").value}"
                val bookcurrentraing = "${snapshot.child("atualreviews").value}"

                totalRating = booktotalrating.toInt()
                currentRating = bookcurrentraing.toFloat()

                //final rating
                finalrating = currentRating / totalRating

                holder.rating.rating = finalrating
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


}