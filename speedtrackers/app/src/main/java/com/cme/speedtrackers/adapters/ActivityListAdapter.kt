package com.cme.speedtrackers.adapters

import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.BottomNavigationActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.ItemActivitiesBinding
import com.cme.speedtrackers.dialogs.ModalDisplayActivity
import com.cme.speedtrackers.model.Atividade
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.database.*
import java.lang.Exception
import java.util.ArrayList

class ActivityListAdapter(private var activityList: ArrayList<Atividade>) : RecyclerView.Adapter<ActivityListAdapter.CustomViewHolder>() {

    //Essential Variables
    private lateinit var binding: ItemActivitiesBinding //View Binding
    private lateinit var context: Context //Context from Parent
    private lateinit var dbRef: DatabaseReference

    public fun setFilteredList(filteredList: ArrayList<Atividade>) {
        this.activityList = filteredList
        notifyDataSetChanged()
    }


    // Number of rows in display
    override fun getItemCount(): Int {
        return activityList.size
    }

    //Creating View with Binding and set Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        binding = ItemActivitiesBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(binding.root)
    }

    // Overriding views with items values and deploy click listeners
    override fun onBindViewHolder(holder: ActivityListAdapter.CustomViewHolder, position: Int) {
        val currentView = activityList[position] // Current ViewItem

        // set Item to Value
        loadImage(holder, currentView.ImageURL)
        currentView.NomeAtividade?.let { holder.tvName.setText(it) }
        currentView.Duracao?.toString().let { holder.tvDuracao.setText(it) }
        currentView.DistanciaPercorrida?.toString().let { holder.tvDistancia.setText(it) }
        holder.tvData.setText(getDataStringFormatted(currentView.Data))

        binding.cardView.setOnClickListener {
            var dialogInfo = ModalDisplayActivity(currentView)
            val activity = context as BottomNavigationActivity
            dialogInfo.show(activity.supportFragmentManager, ContentValues.TAG)
        }
    }

    //Carregar uma imagem recebendo o URL
    private fun loadImage(holder: ActivityListAdapter.CustomViewHolder, imageURL: String) {
        try {
            Glide.with(context).load(imageURL)
                .placeholder(R.drawable.progress_animation)
                .into(holder.ivImagem)
        } catch (e: Exception) {
        }
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

    // Set items Variables
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = binding.tvName
        val tvDistancia = binding.tvDistacia
        val tvDuracao = binding.tvDuracao
        val ivImagem = binding.ivImagem
        val tvData = binding.tvData
    }
}