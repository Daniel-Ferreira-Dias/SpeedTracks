package com.cme.speedtrackers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cme.speedtrackers.adapters.EquipmentListAdapter
import com.cme.speedtrackers.databinding.FragmentEquipmentListBinding
import com.cme.speedtrackers.model.UserEquipment
import com.google.firebase.database.*
import java.util.ArrayList


class EquipmentListFragment : Fragment() {
    private lateinit var binding: FragmentEquipmentListBinding
    private lateinit var equipmentList: ArrayList<UserEquipment>
    private lateinit var dbRef: DatabaseReference
    lateinit var adapter: EquipmentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEquipmentListBinding.inflate(layoutInflater)

        //Type your code
        binding.tvNotFound.visibility = View.GONE
        binding.rvEquipment.hasFixedSize()
        binding.rvEquipment.layoutManager = LinearLayoutManager(requireContext())

        equipmentList = arrayListOf<UserEquipment>()
        adapter = EquipmentListAdapter(equipmentList)
        binding.rvEquipment.adapter = adapter
        getUserEquipments()

        // SearchView
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.toString().isEmpty()){
                    binding.rvEquipment.clearFocus()
                    getUserEquipments()
                }
                else{
                    filter(query.toString())
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                /*if (newText.toString().isEmpty()){
                    getUserEquipments()
                }
                else{
                    filter(newText.toString())
                }*/
                filter(newText.toString())
                return true
            }
        })

        return binding.root
    }

    private fun getUserEquipments() {
        binding.rvEquipment.visibility = View.GONE
        binding.tvCarregando.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Equipments")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                equipmentList.clear()
                if (snapshot.exists()){
                    for (equiSnap in snapshot.children){
                        val equipData = equiSnap.getValue(UserEquipment::class.java)

                        if (equipData?.equipamentoAtivo == true){
                            equipmentList.add(equipData)
                            /*if (equipData?.UserUID == FirebaseAuth.getInstance().uid){
                            equipmentList.add(equipData!!)
                        }*/
                        }
                    }
                    if (equipmentList.size == 0){
                        //binding.rvEquipment.visibility = View.VISIBLE
                        binding.tvCarregando.visibility = View.GONE
                        binding.tvNotFound.visibility = View.VISIBLE
                    }
                    else{
                        binding.rvEquipment.adapter = adapter
                        binding.rvEquipment.visibility = View.VISIBLE
                        binding.tvCarregando.visibility = View.GONE
                        binding.tvNotFound.visibility = View.GONE
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private  fun filter(e: String) {
        //Declare the array list that holds the filtered values
        var filteredItem = ArrayList<UserEquipment>()
        // loop through the array list to obtain the required value
        for (item in equipmentList) {

            if (item.modeloNome.toString().lowercase().contains(e.lowercase())) {
                filteredItem.add(item)
            }
        }
        println("Lista - $filteredItem")
        println("Para query $e - ${filteredItem.size}")
        if (filteredItem.isEmpty()){
            binding.rvEquipment.clearFocus()
            binding.rvEquipment.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
        }
        else{
            adapter.setFilteredList(filteredItem)
            binding.rvEquipment.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        }
    }
}