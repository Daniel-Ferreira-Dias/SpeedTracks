package com.cme.speedtrackers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cme.speedtrackers.adapters.ShopAdapter
import com.cme.speedtrackers.databinding.FragmentShopBinding
import com.cme.speedtrackers.model.Shoes
import com.cme.speedtrackers.model.Shop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


class ShopFragment : Fragment() {
    private lateinit var binding: FragmentShopBinding

    private lateinit var shopList : ArrayList<Shop>
    private lateinit var shopAdapter: ShopAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(layoutInflater)

        //TYPE YOUR CODE HERE
        shopList = arrayListOf<Shop>()
        binding.rvShop.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvShop.hasFixedSize()
        shopAdapter = ShopAdapter(requireContext(), shopList)
        binding.rvShop.adapter = shopAdapter

        getShop()
        getQt()

        return binding.root
    }


    private fun getShop() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Shop")
        dbRef.child("Modelos").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    shopList.clear()
                    for (ds in snapshot.children) {
                        val model = ds.getValue(Shop::class.java)
                        shopList.add(model!!)
                    }
                    binding.rvShop.adapter = shopAdapter
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun getQt() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Shop")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalProducts = "${snapshot.child("Total").value}".toInt()
                binding.tvQtProdutos.text = totalProducts.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}