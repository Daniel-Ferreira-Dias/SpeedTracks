package com.cme.speedtrackers.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cme.speedtrackers.adapters.ShopAdapter
import com.cme.speedtrackers.databinding.FragmentShopBinding
import com.cme.speedtrackers.model.Marcas
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

        //Type your code
        binding.tvNotFound.visibility = View.GONE

        //TYPE YOUR CODE HERE
        shopList = arrayListOf<Shop>()
        binding.rvShop.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvShop.hasFixedSize()
        shopAdapter = ShopAdapter(requireContext(), shopList)
        binding.rvShop.adapter = shopAdapter

        getShop()
        getQt()

        // SearchView
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                return true
            }
        })

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
                    shopAdapter.setFilteredList(shopList)
                    if(shopList.isNotEmpty()){
                        binding.rvShop.adapter = shopAdapter
                        binding.rvShop.visibility = View.VISIBLE
                        binding.tvCarregando.visibility = View.GONE
                        binding.tvNotFound.visibility = View.GONE
                    }else {
                        binding.tvCarregando.visibility = View.GONE
                        binding.tvNotFound.visibility = View.VISIBLE
                    }
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

    private  fun filter(e: String) {
        //Declare the array list that holds the filtered values
        var filteredItem = java.util.ArrayList<Shop>()
        // loop through the array list to obtain the required value
        for (item in shopList) {

            if (item.Nome_Modelo.lowercase().filterNot { it.isWhitespace() }.contains(e.lowercase().filterNot { it.isWhitespace() })) {
                filteredItem.add(item)
            }
        }
        if (filteredItem.isEmpty()){
            binding.rvShop.clearFocus()
            binding.rvShop.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
            shopAdapter.setFilteredList(shopList)
        }
        else{
            shopAdapter.setFilteredList(filteredItem)
            binding.rvShop.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        }
    }
}