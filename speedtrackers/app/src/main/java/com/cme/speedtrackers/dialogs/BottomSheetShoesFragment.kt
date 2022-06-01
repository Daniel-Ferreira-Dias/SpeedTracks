package com.cme.speedtrackers.dialogs

import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.cme.speedtrackers.AddActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.adapters.AddEquipmentToActivityAdapter
import com.cme.speedtrackers.compObj
import com.cme.speedtrackers.databinding.ActivityAddBinding
import com.cme.speedtrackers.databinding.BottomSheetShoesFragmentBinding
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception


class BottomSheetShoesFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetShoesFragmentBinding
    private lateinit var binding2: ActivityAddBinding
    private lateinit var equipmentList : ArrayList<Shoes>
    private lateinit var adapter : AddEquipmentToActivityAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetShoesFragmentBinding.inflate(inflater, container, false)
        binding2 = ActivityAddBinding.inflate(inflater, container, false)
        //Type code here

        // Sapatos
        equipmentList = ArrayList()
        adapter = AddEquipmentToActivityAdapter(equipmentList)
        binding.rvEquipment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvEquipment.adapter = adapter

        loadEquipment()

        binding.tvCancel.setOnClickListener {
            if (compObj.currentDialog.dialog!!.isShowing){
                compObj.currentDialog.dismiss()
                compObj.activityShoe.Shoe_ID = 0
            }
        }
        binding.tvNotFound.visibility = View.VISIBLE
        binding.rvEquipment.visibility = View.VISIBLE
        binding.searchView.clearFocus()
        binding.searchView.setOnClickListener {
            dialog!!.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        binding.searchView.setOnSearchClickListener {
            dialog!!.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                dialog!!.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                dialog!!.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                return true
            }
        })

        return binding.root
    }


    private fun loadEquipment() {
        val ref = FirebaseDatabase.getInstance().getReference("Sapatilhas")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                equipmentList.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(Shoes::class.java)
                    if (model?.EquipamentoAtivo == true){
                        if (model?.Shoe_User_UID == FirebaseAuth.getInstance().uid){
                            equipmentList.add(model!!)
                        }
                    }
                }
                if(equipmentList.isNotEmpty()){
                    equipmentList.reverse()
                    binding.rvEquipment.adapter = adapter
                    binding.rvEquipment.visibility = View.VISIBLE
                    binding.tvNotFound.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        if (equipmentList.isEmpty()){
            binding.rvEquipment.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
        }
    }

    private  fun filter(e: String) {
        //Declare the array list that holds the filtered values
        var filteredItem = java.util.ArrayList<Shoes>()
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (compObj.activityShoe.Shoe_ID != 0L){
            var view = activity?.findViewById<RelativeLayout>(R.id.cl_activity_header)
            view?.visibility = View.VISIBLE
            var view2 = activity?.findViewById<RelativeLayout>(R.id.rl_botoes)
            view2?.visibility = View.GONE
            activity?.currentFocus?.clearFocus()
            var view3 = activity?.findViewById<ImageView>(R.id.iv_imagem)
            loadImage(view3, compObj.activityShoe.ImageURL, requireActivity())
            println(compObj.activityShoe.ImageURL)
            var view4 = activity?.findViewById<TextView>(R.id.tv_modelo)
            view4?.text = compObj.activityShoe.Model_Nome
        }

    }

    private fun loadImage(imageView: ImageView?, imageURL: String, activity: FragmentActivity) {
        try {
            if (imageView != null) {
                Glide.with(activity).load(imageURL)
                    .placeholder(R.drawable.progress_animation)
                    .into(imageView)
            }
        } catch (e: Exception) {
        }
    }
}