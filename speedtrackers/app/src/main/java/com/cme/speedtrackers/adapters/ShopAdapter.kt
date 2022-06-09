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
import com.cme.speedtrackers.BottomNavigationActivity
import com.cme.speedtrackers.EquipmentViewActivity
import com.cme.speedtrackers.ModelosActivity
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

        if (model.Stock < 10) {
            binding.tvWarningStock.visibility = View.VISIBLE
        } else {
            binding.tvWarningStock.visibility = View.GONE
        }

        binding.rlClick.setOnClickListener {
            //var intent = Intent(context, EquipmentViewActivity::class.java)
            //intent.putExtra("List", equipmentList)
            //intent.putExtra("Position", position)
            //context.startActivity(intent)
        }


        mDbRef = FirebaseDatabase.getInstance().getReference("Shop").child("Modelos")
        loadShoe(model, holder)

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

                binding.titleTextView.text = model.Nome_Modelo
                binding.tvPrice.text = model.Preço.toString()


            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}