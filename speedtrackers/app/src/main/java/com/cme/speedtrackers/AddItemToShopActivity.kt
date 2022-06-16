package com.cme.speedtrackers

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.text.set
import androidx.core.widget.addTextChangedListener
import com.cme.speedtrackers.databinding.ActivityAddItemToShopBinding
import com.cme.speedtrackers.databinding.ActivityAddModelBinding
import com.cme.speedtrackers.model.Marcas
import com.cme.speedtrackers.model.Modelos
import com.cme.speedtrackers.model.Shop
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AddItemToShopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddItemToShopBinding

    var nomeModelo = ""
    var IDMarca = ""
    var nomeMarca = ""
    var IDModel = ""
    var Price = ""
    var Stock = ""
    var Descrição = ""

    var uploadImageUrl_Marca = ""
    var uploadImageUrl_Modelo = ""

    val ModelosList = ArrayList<Int>()
    val MarcasList = ArrayList<Int>()
    val ModelosNameList = ArrayList<String>()

    override fun onResume() {
        super.onResume()
        loadMarcasId()
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, MarcasList)
        binding.etMarcaId.setAdapter(arrayAdapter)

        loadModelID()
        val arrayAdapterModel = ArrayAdapter(this, R.layout.dropdown_item, ModelosList)
        binding.etModelId.setAdapter(arrayAdapterModel)

        loadModelID()
        val arrayAdapterModel_Name = ArrayAdapter(this, R.layout.dropdown_item, ModelosNameList)
        binding.etModelName.setAdapter(arrayAdapterModel_Name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemToShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etMarcaId.inputType = 0
        binding.etModelId.inputType = 0
        binding.etModelName.inputType = 0

        binding.etMarcaId.addTextChangedListener {
            IDMarca = binding.etMarcaId.text.toString()
            loadModelID()
        }

        binding.etModelId.addTextChangedListener {
            IDModel = binding.etModelId.text.toString()
            loadModelID()
        }


        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        binding.ibGoBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            checkConditions()
        }
    }

    private fun loadMarcasId(){
        val mref = FirebaseDatabase.getInstance().getReference("Marcas")
        mref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    val model = ds.getValue(Marcas::class.java)
                    MarcasList.add(model!!.ID.toInt())
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun loadModelID(){
        ModelosList.clear()
        ModelosNameList.clear()
        val mref = FirebaseDatabase.getInstance().getReference("Marcas").child(IDMarca).child("Modelos")
        mref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    val model = ds.getValue(Modelos::class.java)
                    ModelosList.add(model!!.ID_Modelo.toInt())
                    if (IDModel == model.ID_Modelo.toString()){
                        ModelosNameList.add(model.Nome_Modelo)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun checkConditions() {
        IDMarca = binding.etMarcaId.text.toString()
        nomeModelo = binding.etModelName.text.toString()
        IDModel = binding.etModelId.text.toString()
        Price = binding.etPrice.text.toString()
        Stock = binding.etStock.text.toString()

        if (nomeModelo.isEmpty()) {
            Toast.makeText(this, "Insira o nome do Modelo", Toast.LENGTH_SHORT).show()
        } else if (IDMarca.isEmpty() || IDModel == "Marca ID") {
            Toast.makeText(this, "Insira o ID da Marca", Toast.LENGTH_SHORT).show()
        } else if (IDModel.isEmpty() || IDModel == "Modelo ID") {
            Toast.makeText(this, "Insira o ID do Modelo", Toast.LENGTH_SHORT).show()
        } else if (Price.isEmpty()) {
            Toast.makeText(this, "Insira o Preço", Toast.LENGTH_SHORT).show()
        } else if (Stock.isEmpty()) {
            Toast.makeText(this, "Insira o Stock", Toast.LENGTH_SHORT).show()
        }else {
            checkDataBase()
        }

        Log.d("MODELO", IDModel)

    }

    private fun checkDataBase() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Shop").child("Modelos")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(IDModel)) {
                } else {
                    addModelo()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addModelo() {

        // set up data
        val hashMap = HashMap<String, Any>()
        hashMap["ID_Modelo"] = IDModel.toInt()
        hashMap["ID_Marca"] = IDMarca.toInt()
        hashMap["Nome_Modelo"] = nomeModelo
        hashMap["Preço"] = Price.toInt()
        hashMap["Stock"] = Stock.toInt()
        hashMap["Descrição"] = Descrição

        val mref = FirebaseDatabase.getInstance().getReference("Marcas").child(IDMarca)
        mref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                nomeMarca = "${snapshot.child("Nome").value}"
                uploadImageUrl_Marca = "${snapshot.child("Imagem").value}"

                hashMap["Nome_Marca"] = nomeMarca
                hashMap["Imagem_Marca"] = uploadImageUrl_Marca

                val tref = FirebaseDatabase.getInstance().getReference("Marcas").child(IDMarca).child("Modelos").child(IDModel)
                tref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        uploadImageUrl_Modelo = "${snapshot.child("Imagem_Modelo").value}"
                        hashMap["Imagem_Modelo"] = uploadImageUrl_Modelo
                        // Save to DB
                        val ref = FirebaseDatabase.getInstance().getReference("Shop").child("Modelos")
                        ref.child(IDModel)
                            .setValue(hashMap)
                            .addOnSuccessListener {
                                Toast.makeText(this@AddItemToShopActivity, "Item adicionado com sucesso", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@AddItemToShopActivity, BottomNavigationActivity::class.java))
                                finish()
                            }

                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })






            }
            override fun onCancelled(error: DatabaseError) {
            }
        })



    }
}