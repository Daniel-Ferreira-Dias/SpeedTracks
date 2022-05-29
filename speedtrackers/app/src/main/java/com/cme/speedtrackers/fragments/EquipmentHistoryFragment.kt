package com.cme.speedtrackers.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cme.speedtrackers.adapters.EquipmentHistoryAdapter
import com.cme.speedtrackers.databinding.FragmentEquipmentHistoryBinding
import com.cme.speedtrackers.model.UserEquipment
import com.google.firebase.database.*
import java.util.ArrayList


class EquipmentHistoryFragment : Fragment() {

    lateinit var binding: FragmentEquipmentHistoryBinding
    private lateinit var equipmentList: ArrayList<UserEquipment>
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEquipmentHistoryBinding.inflate(layoutInflater)

        //Type your code
        binding.tvNotFound.visibility = View.GONE
        binding.rvEquipment.layoutManager = LinearLayoutManager(requireContext())

        equipmentList = arrayListOf<UserEquipment>()
        getUserEquipments()


        return binding.root
    }

    private fun getUserEquipments() {
        binding.rvEquipment.visibility = View.GONE
        binding.tvCarregando.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Equipments")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                equipmentList.clear()
                if (snapshot.exists()){
                    for (equiSnap in snapshot.children){
                        val equipData = equiSnap.getValue(UserEquipment::class.java)
                        if (equipData?.equipamentoAtivo == false){
                            /*if (equipData?.UserUID == FirebaseAuth.getInstance().uid){
                            equipmentList.add(equipData!!)
                        }*/
                            equipmentList.add(equipData!!)
                        }
                    }
                    if (equipmentList.size == 0){
                        //binding.rvEquipment.visibility = View.VISIBLE
                        binding.tvCarregando.visibility = View.GONE
                        binding.tvNotFound.visibility = View.VISIBLE
                    }
                    else{
                        binding.rvEquipment.adapter = EquipmentHistoryAdapter(equipmentList)
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

}