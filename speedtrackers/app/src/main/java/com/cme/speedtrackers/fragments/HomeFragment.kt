package com.cme.speedtrackers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.BottomNavigationActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.adapters.ActivityListAdapter
import com.cme.speedtrackers.adapters.EquipmentListAdapter
import com.cme.speedtrackers.adapters.ShoeAdapater
import com.cme.speedtrackers.databinding.FragmentHomeBinding
import com.cme.speedtrackers.model.Atividade
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var activityList: ArrayList<Atividade>
    private lateinit var dbRef: DatabaseReference
    lateinit var activityAdapater: ActivityListAdapter

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
        activityList = arrayListOf<Atividade>()
        binding.activityEquipament.layoutManager = LinearLayoutManager(requireContext())
        activityAdapater = ActivityListAdapter(activityList)
        binding.activityEquipament.adapter = activityAdapater
        getUserActivities()



        shoeList = arrayListOf<Shoes>()
        binding.shoeRecycler.hasFixedSize()
        shoeAdapter = ShoeAdapater(requireContext(), shoeList)
        binding.shoeRecycler.adapter = shoeAdapter
        getUserShoes()

        getUserName()

        binding.seeMoreShoes.setOnClickListener {
            var viewPagerActivity: BottomNavigationActivity = activity as BottomNavigationActivity
            viewPagerActivity.modificarPosicao(2)
        }
        binding.seeMoreActivities.setOnClickListener {
            var viewPagerActivity: BottomNavigationActivity = activity as BottomNavigationActivity
            viewPagerActivity.modificarPosicao(3)
        }

        return binding.root
    }

    private fun getUserActivities() {
        dbRef = FirebaseDatabase.getInstance().getReference("Atividades")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                activityList.clear()
                var tempActivity = ArrayList<Atividade>()
                for (ds in snapshot.children) {
                    val model = ds.getValue(Atividade::class.java)
                    if (model?.User_UID == FirebaseAuth.getInstance().uid) {
                        tempActivity.add(model!!)
                    }
                }
                tempActivity.reverse()
                for (atividade in tempActivity){
                    activityList.add(atividade)
                    if (activityList.size == 5){
                        break
                    }
                }
                binding.activityEquipament.adapter = activityAdapater
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
                var tempList = ArrayList<Shoes>()
                for (ds in snapshot.children) {
                    val model = ds.getValue(Shoes::class.java)
                    if (model?.Shoe_User_UID == FirebaseAuth.getInstance().uid) {
                        tempList.add(model!!)
                    }
                }
                tempList.sortByDescending { it.KmTraveled }
                for (shoe in tempList){
                    shoeList.add(shoe)
                    if (shoeList.size == 3) {
                        break
                    }
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