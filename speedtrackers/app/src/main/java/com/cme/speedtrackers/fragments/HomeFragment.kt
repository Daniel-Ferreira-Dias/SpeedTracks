package com.cme.speedtrackers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.R
import com.cme.speedtrackers.adapters.EquipmentListAdapter
import com.cme.speedtrackers.adapters.ShoeAdapater
import com.cme.speedtrackers.databinding.FragmentHomeBinding
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var equipmentList: ArrayList<Shoes>
    private lateinit var dbRef: DatabaseReference
    lateinit var equipamentAdapater: EquipmentListAdapter

    private lateinit var shoeList : ArrayList<Shoes>
    lateinit var shoeAdapter: ShoeAdapater


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.shoeRecycler.hasFixedSize()
        binding.shoeRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        //TYPE YOUR CODE HERE
        equipmentList = arrayListOf<Shoes>()
        equipamentAdapater = EquipmentListAdapter(equipmentList)
        binding.shoeRecycler.adapter = equipamentAdapater
        getUserEquipments()

        binding.recylcerEquipament.layoutManager =
            LinearLayoutManager(requireContext())

        shoeList = arrayListOf<Shoes>()
        shoeAdapter = ShoeAdapater(requireContext(), shoeList)
        binding.recylcerEquipament.adapter = shoeAdapter
        getUserShoes()

        getUserName()

        return binding.root
    }

    private fun getUserEquipments() {

        dbRef = FirebaseDatabase.getInstance().getReference("Sapatilhas")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                equipmentList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(Shoes::class.java)
                    if (model?.Shoe_User_UID == FirebaseAuth.getInstance().uid) {
                        equipmentList.add(model!!)
                    }
                }
                binding.recylcerEquipament.adapter = equipamentAdapater
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getUserShoes() {

        dbRef = FirebaseDatabase.getInstance().getReference("Sapatilhas")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shoeList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(Shoes::class.java)
                    if (model?.Shoe_User_UID == FirebaseAuth.getInstance().uid) {
                        shoeList.add(model!!)
                    }
                    shoeList.sortByDescending { it.KmTraveled }
                }
                binding.shoeRecycler.adapter = shoeAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getUserName() {

        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userName = "${snapshot.child("userName").value}"
                    binding.userName.text = userName
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


}