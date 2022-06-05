package com.cme.speedtrackers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.ItemEquipmentListBinding
import com.cme.speedtrackers.model.Modelos
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.database.*
import java.lang.Exception
import java.util.ArrayList

class EquipmentHistoryAdapter(private var equipmentList: ArrayList<Shoes>) : RecyclerView.Adapter<EquipmentHistoryAdapter.CustomViewHolder>()  {

    //Essential Variables
    private lateinit var binding: ItemEquipmentListBinding //View Binding
    private lateinit var context: Context //Context from Parent
    private lateinit var dbRef: DatabaseReference

    public fun setFilteredList(filteredList: ArrayList<Shoes>) {
        this.equipmentList = filteredList
        notifyDataSetChanged()
    }

    // Number of rows in display
    override fun getItemCount(): Int {
        return equipmentList.size
    }

    //Creating View with Binding and set Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        binding = ItemEquipmentListBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(binding.root)
    }

    // Overriding views with items values and deploy click listeners
    override fun onBindViewHolder(holder: EquipmentHistoryAdapter.CustomViewHolder, position: Int) {
        val currentView = equipmentList[position] // Current ViewItem

        // set Item to Value
        setImage(holder, currentView)
        holder.tvData.setText(getDataStringFormatted(currentView.FirstUsage.toString()))
        currentView.KmTraveled?.toString().let { holder.tvDistancia.setText(it) }
        holder.tvName.text = currentView.Shoe_Nome.toString()
    }

    private fun setImage(holder: EquipmentHistoryAdapter.CustomViewHolder, currentView: Shoes) {
        loadImage(holder, currentView.ImageURL.toString())
    }
    //Carregar uma imagem recebendo o URL
    private fun loadImage(holder: EquipmentHistoryAdapter.CustomViewHolder, imageURL: String) {
        try {
            Glide.with(context).load(imageURL)
                .placeholder(R.drawable.progress_animation)
                .into(holder.ivImagem)
        } catch (e: Exception) {
        }
    }
    // Set items Variables
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = binding.tvName
        val tvDistancia = binding.tvDistacia
        val tvData = binding.tvFirstUseDate
        val ivImagem = binding.ivImagem
    }

    private fun getDataStringFormatted(string: String) : String{
        var result: String = ""
        var replacedString = string.replace("-", "")
        var dia = replacedString.subSequence(0, 2)
        var mes = replacedString.subSequence(2, 4)
        var ano = replacedString.subSequence(4, 8)
        var mesName = ""
        when (mes){
            "01" -> mesName = "Jan"
            "02" -> mesName = "Fev"
            "03" -> mesName = "Mar"
            "04" -> mesName = "Abr"
            "05" -> mesName = "Maio"
            "06" -> mesName = "Jun"
            "07" -> mesName = "Jul"
            "08" -> mesName = "Ago"
            "09" -> mesName = "Set"
            "10" -> mesName = "Out"
            "11" -> mesName = "Nov"
            "12" -> mesName = "Dez"
        }

        result = "$dia, $mesName de $ano"
        return result
    }
}