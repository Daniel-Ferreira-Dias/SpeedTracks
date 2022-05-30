package com.cme.speedtrackers.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.cme.speedtrackers.AddActivity
import com.cme.speedtrackers.adapters.ActivityListAdapter
import com.cme.speedtrackers.databinding.FragmentActivityBinding
import com.cme.speedtrackers.model.Atividade
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class ActivityFragment : Fragment() {
    private lateinit var binding: FragmentActivityBinding
    private lateinit var activityList: ArrayList<Atividade>
    private lateinit var dbRef: DatabaseReference
    lateinit var adapter: ActivityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivityBinding.inflate(layoutInflater)

        //Type your code
        binding.floatingBtn2.isEnabled = true
        binding.floatingBtn2.isClickable = true
        binding.floatingBtn2.setOnClickListener {
            val intent = Intent(this.requireContext(), AddActivity::class.java)
            startActivity(intent)
        }

        binding.tvNotFound.visibility = View.GONE
        binding.rvActivity.hasFixedSize()
        binding.rvActivity.layoutManager = LinearLayoutManager(requireContext())

        activityList = arrayListOf<Atividade>()
        adapter = ActivityListAdapter(activityList)
        binding.rvActivity.adapter = adapter
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


        return binding.root
    }

    //FUNCTIONS
    private fun getUserEquipments() {
        binding.rvActivity.visibility = View.GONE
        binding.tvCarregando.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Atividades")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                activityList.clear()
                if (snapshot.exists()){
                    for (atividadeSnap in snapshot.children){
                        val atividadeData = atividadeSnap.getValue(Atividade::class.java)
                        if (atividadeData?.User_UID == FirebaseAuth.getInstance().uid){
                            activityList.add(atividadeData!!)
                        }
                    }
                    if (activityList.size == 0){
                        //binding.rvEquipment.visibility = View.VISIBLE
                        binding.tvCarregando.visibility = View.GONE
                        binding.tvNotFound.visibility = View.VISIBLE
                    }
                    else{
                        binding.rvActivity.adapter = adapter
                        binding.rvActivity.visibility = View.VISIBLE
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
        var filteredItem = ArrayList<Atividade>()
        // loop through the array list to obtain the required value
        for (item in activityList) {

            if (item.NomeAtividade.toString().lowercase().filterNot { it.isWhitespace() }.contains(e.lowercase().filterNot { it.isWhitespace() })) {
                filteredItem.add(item)
            }
        }
        println("Lista - $filteredItem")
        println("Para query $e - ${filteredItem.size}")
        if (filteredItem.isEmpty()){
            binding.rvActivity.clearFocus()
            binding.rvActivity.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
        }
        else{
            adapter.setFilteredList(filteredItem)
            binding.rvActivity.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        binding.searchView.clearFocus()
    }
}