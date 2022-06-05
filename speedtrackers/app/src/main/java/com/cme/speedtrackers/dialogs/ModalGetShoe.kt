package com.cme.speedtrackers.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cme.speedtrackers.EquipmentViewActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.adapters.AddEquipmentToActivityAdapter
import com.cme.speedtrackers.adapters.SimpleGetShoeAdapter
import com.cme.speedtrackers.databinding.ModalGetShoeBinding
import com.cme.speedtrackers.model.Shoes
import java.util.ArrayList


class ModalGetShoe(var list: ArrayList<Shoes>, var position: Int, var atividade: EquipmentViewActivity) : DialogFragment() {
    private lateinit var binding: ModalGetShoeBinding
    private lateinit var adapter : SimpleGetShoeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ModalGetShoeBinding.inflate(inflater, container, false)

        //TYPE YOUR CODE
        adapter = SimpleGetShoeAdapter(list, position, this, atividade)
        binding.rvEquipment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvEquipment.adapter = adapter

        binding.tvNotFound.visibility = View.VISIBLE
        binding.rvEquipment.visibility = View.VISIBLE
        binding.searchView.clearFocus()

        Log.e("DIALOG", "Recebi: $position | ${list.size}")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners(view: View) {
        binding.tvCancel.setOnClickListener { dismiss() }

        //SEARCH VIEW
        binding.searchView.setOnClickListener {
            dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        binding.searchView.setOnSearchClickListener {
            dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                return true
            }
        })
    }

    private  fun filter(e: String) {
        //Declare the array list that holds the filtered values
        var filteredItem = java.util.ArrayList<Shoes>()

        // loop through the array list to obtain the required value
        for (item in list) {
            if (item.Shoe_Nome.toString().filterNot { it.isWhitespace() }.lowercase().contains(e.lowercase().filterNot { it.isWhitespace() })) {
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
            Log.e("FILTER: ", "Enviei uma lista de tamanho ${filteredItem.size}")
            for (x in filteredItem){
                Log.e("FILTER_ITEM: ", "${x.Shoe_Nome}")
            }
            adapter.setFilteredList(filteredItem)
            binding.rvEquipment.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        }
    }
}