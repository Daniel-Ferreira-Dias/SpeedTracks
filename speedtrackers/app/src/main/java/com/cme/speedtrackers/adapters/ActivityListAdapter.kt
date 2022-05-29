package com.cme.speedtrackers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.ItemActivitiesBinding
import com.cme.speedtrackers.databinding.ItemEquipmentListBinding
import com.cme.speedtrackers.model.Atividade
import com.cme.speedtrackers.model.Modelos
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
        setModeloNameAndImage(currentView.Shoe_ID, holder, currentView)
        currentView.NomeAtividade?.let { holder.tvName.setText(it) }
        currentView.Duracao?.let { holder.tvDuracao.setText(it) }
        currentView.DistanciaPercorrida?.toString().let { holder.tvDistancia.setText(it) }
    }

    private fun setModeloNameAndImage(
        shoeID: Long?,
        holder: ActivityListAdapter.CustomViewHolder,
        currentItem: Atividade
    ) {
        dbRef = FirebaseDatabase.getInstance().getReference("Sapatilhas/${shoeID}")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var model = snapshot.getValue(Shoes::class.java)
                    /*if (model?.ImageURL.toString().length.compareTo(10) <= 0 ){
                        FirebaseDatabase.getInstance().getReference("Sapatilhas").child(currentItem.Shoe_ID.toString()).child("ImageURL")
                            .setValue(model?.Imagem_Modelo.toString())
                    }*/
                    loadImage(holder, model?.ImageURL.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
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

    // Set items Variables
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = binding.tvName
        val tvDistancia = binding.tvDistacia
        val tvDuracao = binding.tvDuracao
        val ivImagem = binding.ivImagem
    }
}