package com.cme.speedtrackers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.ActivityModelosBinding
import com.cme.speedtrackers.databinding.ActivityResumeShoeBinding
import com.cme.speedtrackers.databinding.FragmentActivityBinding
import com.cme.speedtrackers.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


class ResumeShoeActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityResumeShoeBinding

    //Get variables
    val compObj = GlobalClass.Companion

    //Set variables
    var marcaId = ""
    var modelId = ""
    var corId = ""
    var corNome = ""
    var shoeSize = ""

    // Get user
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeShoeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        marcaId = compObj.Brand_ID
        modelId = compObj.Model_ID
        corId = compObj.Color_ID


        loadBrand()
        loadColor()
        loadImageBrand()

        binding.cancelButton.setOnClickListener {
            Toast.makeText(this, "Processo cancelado", Toast.LENGTH_SHORT)
            startActivity(Intent(this, BottomNavigationActivity::class.java))
            finish()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.addShoe.setOnClickListener {
            shoeSize = binding.ShoeSize.text.toString()

            if (shoeSize.isEmpty()){
                Toast.makeText(this, "Insira o tamanho da sapatilha", Toast.LENGTH_SHORT).show()
            }else{
                if (mAuth.currentUser != null) {
                    addShoe()
                } else {
                    Toast.makeText(
                        this,
                        "Tens que estar logado para adicionar a sapatilha",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun loadImageBrand() {
        val ref = FirebaseDatabase.getInstance().getReference("Marcas")
        ref.child(marcaId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val imagem_Marca = "${snapshot.child("Imagem").value}"
                try {
                    Glide.with(this@ResumeShoeActivity).load(imagem_Marca)
                        .placeholder(R.drawable.progress_animation)
                        .into(binding.imageBrand)
                } catch (e: Exception) {
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun loadBrand() {
        //Modelos
        val ref = FirebaseDatabase.getInstance().getReference("Marcas")
        ref.child(marcaId).child("Modelos").child(modelId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nome_modelo = "${snapshot.child("Nome_Modelo").value}"
                    val imagem_modelo = "${snapshot.child("Imagem_Modelo").value}"

                    binding.shoeName.text = nome_modelo

                    try {
                        Glide.with(this@ResumeShoeActivity).load(imagem_modelo)
                            .placeholder(R.drawable.progress_animation)
                            .into(binding.imagemShoe)
                    } catch (e: Exception) {
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun loadColor() {
        val ref = FirebaseDatabase.getInstance().getReference("Cores")
        ref.child(corId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val imagem_cor = "${snapshot.child("Imagem_Cor").value}"
                    val nome_cor = "${snapshot.child("Nome_Cor").value}"

                    binding.colorName.text = nome_cor
                    corNome = nome_cor
                    try {
                        Glide.with(this@ResumeShoeActivity).load(imagem_cor)
                            .placeholder(R.drawable.progress_animation)
                            .into(binding.colorPicture)
                    } catch (e: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun addShoe() {
        val timestamp = System.currentTimeMillis()

        // set up data
        val hashMap = HashMap<String, Any>()
        hashMap["Brand_Nome"] = compObj.Brand_Name
        hashMap["Model_Nome"] = compObj.Model_Name
        hashMap["Color_Nome"] = corNome
        hashMap["Brand_ID"] = marcaId
        hashMap["Model_ID"] = modelId
        hashMap["Shoe_Size"] = shoeSize.toLong()
        hashMap["Shoe_ID"] = timestamp
        hashMap["Shoe_TimeStamp"] = timestamp
        hashMap["Shoe_User_UID"] = mAuth.uid.toString()

        // Save to DB
        val ref = FirebaseDatabase.getInstance().getReference("Sapatilhas")
        ref.child(timestamp.toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Sapatilha adicionada com sucesso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, BottomNavigationActivity::class.java))
                finish()
            }
    }
}