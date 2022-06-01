package com.cme.speedtrackers.adapters


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.EquipmentViewActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.SimpleShoeListItemBinding
import com.cme.speedtrackers.dialogs.ModalGetShoe
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.database.*
import java.lang.Exception
import java.util.ArrayList
import kotlin.properties.Delegates


class SimpleGetShoeAdapter(private var equipmentList: ArrayList<Shoes>, private var currentPosition: Int, private var modal: ModalGetShoe, var atividade: EquipmentViewActivity) : RecyclerView.Adapter<SimpleGetShoeAdapter.CustomViewHolder>() {

    //Essential Variables
    private lateinit var binding: SimpleShoeListItemBinding //View Binding
    private lateinit var context: Context //Context from Parent
    private lateinit var dbRef: DatabaseReference
    private var localList: ArrayList<Shoes> = equipmentList
    private var isChanged: Boolean = false

    public fun setFilteredList(filteredList: ArrayList<Shoes>) {
        this.localList = filteredList
        isChanged = localList.size != equipmentList.size
        notifyDataSetChanged()
    }


    // Number of rows in display
    override fun getItemCount(): Int {
        return localList.size
    }

    //Creating View with Binding and set Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        binding = SimpleShoeListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(binding.root)
    }

    // Overriding views with items values and deploy click listeners
    override fun onBindViewHolder(holder: SimpleGetShoeAdapter.CustomViewHolder, position: Int) {
        var currentView = localList[position] // Current ViewItem


        // set Item to Value
        setImage(holder, currentView)
        holder.tvName.text = currentView?.Shoe_Nome.toString()
        holder.tvName.setTextColor(context.resources.getColor(R.color.defaultText))
        if (localList[position].Shoe_ID == equipmentList[currentPosition].Shoe_ID){
            holder.tvName.setTextColor(context.resources.getColor(R.color.primary))
        }

        //selectedIndicator Logic
        /*if (!isChanged){
            if (localList[position].Shoe_ID == equipmentList[currentPosition].Shoe_ID){
                holder.tvName.setTextColor(context.resources.getColor(R.color.primary))
            }
        }
        else{

        }*/


        //Click Listener
        binding.cardView.setOnClickListener {
            if (isChanged){
                //println("AQUIIIII:  ${localList[position].Shoe_ID} != ${equipmentList[currentPosition].Shoe_ID}")
                if (localList[position].Shoe_ID != equipmentList[currentPosition].Shoe_ID && position < localList.size){
                    var primaryPosition = 0
                    for (item in equipmentList){
                        if (item.Shoe_ID == localList[position].Shoe_ID && position < localList.size){
                            break
                        }
                        primaryPosition += 1
                    }
                    currentPosition = primaryPosition
                    Log.e("PositionAlteredList: ", "$position | ${localList.size}")
                    Log.e("IsChanged: ", "$currentPosition | ${equipmentList.size}")
                    atividade.changeViews(currentPosition, equipmentList)
                    modal.dismiss()
                }
                else{
                    Toast.makeText(context, "Este calçado já está selecionado", Toast.LENGTH_SHORT).show()
                }
            }
            else if (!isChanged){
                println("isCHANGED = FALSE")
                if (position != currentPosition){
                    atividade.changeViews(position, equipmentList)
                    modal.dismiss()
                }
                else{
                    Toast.makeText(context, "Este calçado já está selecionado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setImage(
        holder: SimpleGetShoeAdapter.CustomViewHolder,
        currentItem: Shoes
    ) {
        loadImage(holder, currentItem.ImageURL)
    }

    //Carregar uma imagem recebendo o URL
    private fun loadImage(holder: SimpleGetShoeAdapter.CustomViewHolder, imageURL: String) {
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
        val ivImagem = binding.ivImagem
    }

}