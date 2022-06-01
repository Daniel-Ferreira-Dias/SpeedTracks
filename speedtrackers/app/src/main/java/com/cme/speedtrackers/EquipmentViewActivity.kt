package com.cme.speedtrackers

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cme.speedtrackers.adapters.ActivityListAdapter
import com.cme.speedtrackers.databinding.ActivityEquipmentViewBinding
import com.cme.speedtrackers.dialogs.ModalChangeName
import com.cme.speedtrackers.dialogs.ModalConfirmRemoval
import com.cme.speedtrackers.model.Atividade
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception
import kotlin.properties.Delegates

private lateinit var binding: ActivityEquipmentViewBinding
private lateinit var activityAdapater: ActivityListAdapter
private lateinit var activityList: java.util.ArrayList<Atividade>
private lateinit var dbRef: DatabaseReference
private lateinit var list: java.util.ArrayList<Shoes>
private var currentPosition by Delegates.notNull<Int>()

class EquipmentViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquipmentViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        val intent = intent
        list  = intent.getSerializableExtra("List") as ArrayList<Shoes>
        currentPosition = intent.getIntExtra("Position", 0)

        //First View
        changeViews(currentPosition, list)


        //Click Listeners
        binding.ivBuy.setOnClickListener {
            val intent = Intent(this, BottomNavigationActivity::class.java)
            intent.putExtra("Store", 1)
            startActivity(intent)
        }
        binding.ibLeftArrow.setOnClickListener {
            var newPosition = currentPosition - 1
            changeViews(newPosition, list)
        }
        binding.ibRightArrow.setOnClickListener {
            var newPosition = currentPosition + 1
            println("Before change: $currentPosition | ${list.size}")
            changeViews(newPosition, list)
            println("after change: $currentPosition | ${list.size}")
        }
        binding.btnRemove.setOnClickListener {
            var dialog = ModalConfirmRemoval(list, currentPosition, this@EquipmentViewActivity)
            dialog.show(supportFragmentManager, ContentValues.TAG)
        }
        binding.btnEdit.setOnClickListener {
            var dialog = ModalChangeName(list[currentPosition], list, currentPosition, this@EquipmentViewActivity)
            dialog.show(supportFragmentManager, ContentValues.TAG)
        }
    }

    private fun updateListandPosition(listaNew: ArrayList<Shoes>, positionNew: Int){
        list = listaNew
        currentPosition = positionNew
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarSettingsActivity)
        val actionBar = supportActionBar

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white_24)
        }
        actionBar?.title = ""
        binding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }
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

    private fun loadImage(imageV: ImageView, imageURL: String) {
        try {
            Glide.with(this).load(imageURL)
                .placeholder(R.drawable.progress_animation)
                .into(imageV)
        } catch (e: Exception) {
        }
    }

    public fun changeViews(position: Int, list: ArrayList<Shoes>){
        updateListandPosition(list, position)

        activityList = arrayListOf<Atividade>()
        binding.rvAtividades.layoutManager = LinearLayoutManager(this)
        binding.rvAtividades.hasFixedSize()
        activityAdapater = ActivityListAdapter(activityList)
        binding.rvAtividades.adapter = activityAdapater
        getUserActivities(list, position)

        binding.btnNomeModelo.text = list[position].Shoe_Nome
        binding.tvMarca.text = list[position].Brand_Nome
        binding.tvModelo.text = list[position].Model_Nome
        binding.tvData.text = getDataStringFormatted(list[position].FirstUsage.toString())
        binding.tvTamanho.text = list[position].Shoe_Size.toString()
        binding.tvKm.text = list[position].KmTraveled.toString()
        var restantes =  600 - list[position].KmTraveled.toString().toDouble()

        if (restantes.compareTo(600) <= 0){
            binding.tvKmRestantes.text = restantes.toString()
        }
        else if(restantes.compareTo(0) <= 0){
            binding.tvKmRestantes.text = "Limite atingido"
        }

        if (list[position].EquipamentoAtivo == true){
            binding.tvStatus.text = "Ativo"
            binding.tvStatus.setTextColor(resources.getColor(R.color.primary))
        }
        else if (list[position].EquipamentoAtivo == false){
            binding.tvStatus.text = "Removido"
            binding.tvStatus.setTextColor(resources.getColor(R.color.red))
        }

        binding.btnRemove.visibility = View.VISIBLE
        if (binding.tvStatus.text == "Removido"){
            binding.btnRemove.visibility = View.GONE
        }

        loadImage(binding.ivImagem, list[position].ImageURL)
        loadImage(binding.ivCor, list[position].ColorURL)

        binding.ibLeftArrow.visibility = View.VISIBLE
        binding.ibRightArrow.visibility = View.VISIBLE
        if (position == 0 && list.size.toInt() == 1){
            binding.ibRightArrow.visibility = View.GONE
            binding.ibLeftArrow.visibility = View.GONE
        }
        else if (position == list.size.toInt() - 1){
            binding.ibRightArrow.visibility = View.GONE
        }
        else if (position == 0){
            binding.ibLeftArrow.visibility = View.GONE
        }
    }

    private fun getUserActivities(list: java.util.ArrayList<Shoes>, position: Int) {
        dbRef = FirebaseDatabase.getInstance().getReference("Atividades")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                activityList.clear()
                var tempActivity = java.util.ArrayList<Atividade>()
                for (ds in snapshot.children) {
                    val model = ds.getValue(Atividade::class.java)
                    if (model?.User_UID == FirebaseAuth.getInstance().uid && model?.Shoe_ID == list[position].Shoe_ID) {
                        tempActivity.add(model!!)
                    }
                }
                tempActivity.reverse()
                for (atividade in tempActivity){
                    activityList.add(atividade)
                    if (activityList.size == 15){
                        break
                    }
                }
                binding.rvAtividades.adapter = activityAdapater
                activityAdapater.setFilteredList(activityList)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        if (activityList.isEmpty()){
            binding.tvNotfound.visibility = View.VISIBLE
        }
        else{
            binding.tvNotfound.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        binding.btnNomeModelo.clearFocus()
    }
}