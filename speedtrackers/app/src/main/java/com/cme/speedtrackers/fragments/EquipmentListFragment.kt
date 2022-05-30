package com.cme.speedtrackers.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.MarcasActivity
import com.cme.speedtrackers.adapters.EquipmentListAdapter
import com.cme.speedtrackers.databinding.FragmentEquipmentListBinding
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList


class EquipmentListFragment : Fragment() {
    private lateinit var binding: FragmentEquipmentListBinding
    private lateinit var equipmentList: ArrayList<Shoes>
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

        equipmentList = arrayListOf<Shoes>()
        adapter = EquipmentListAdapter(equipmentList)
        binding.rvEquipment.adapter = adapter
        getUserEquipments()

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

        binding.floatingBtn.isEnabled = true
        binding.floatingBtn.setOnClickListener {
            val intent = Intent(this.requireContext(), MarcasActivity::class.java)
            startActivity(intent)
        }

        binding.rvEquipment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.floatingBtn.isShown()) binding.floatingBtn.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) binding.floatingBtn.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        return binding.root
    }

    private fun getUserEquipments() {
        binding.rvEquipment.visibility = View.GONE
        binding.tvCarregando.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Sapatilhas")
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                equipmentList.clear()
                if (snapshot.exists()){
                    for (equiSnap in snapshot.children){
                        val equipData = equiSnap.getValue(Shoes::class.java)
                        if (equipData?.EquipamentoAtivo == true){
                            if (equipData?.Shoe_User_UID == FirebaseAuth.getInstance().uid){
                                equipmentList.add(equipData!!)
                            }
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
    override fun onResume() {
        super.onResume()
        binding.searchView.clearFocus()
    }

    private  fun filter(e: String) {
        //Declare the array list that holds the filtered values
        var filteredItem = ArrayList<Shoes>()
        // loop through the array list to obtain the required value
        for (item in equipmentList) {

            if (item.Model_Nome.toString().filterNot { it.isWhitespace() }.lowercase().contains(e.lowercase().filterNot { it.isWhitespace() })) {
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