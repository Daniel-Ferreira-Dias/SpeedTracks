package com.cme.speedtrackers

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cme.speedtrackers.databinding.ActivityAddMarca2Binding
import com.cme.speedtrackers.model.Marcas
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AddMarcaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMarca2Binding

    var nomeMarca = ""
    var IDMarca = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMarca2Binding.inflate(layoutInflater)
        setContentView(binding.root)

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



    private fun checkConditions() {
        IDMarca = binding.etMarcaId.text.toString()
        nomeMarca = binding.etNomeMarca.text.toString()

        if (nomeMarca.isEmpty()) {
            Toast.makeText(this, "Insira o nome da Marca", Toast.LENGTH_SHORT).show()
        } else if (IDMarca.toString().isEmpty()) {
            Toast.makeText(this, "Insira o ID da Marca", Toast.LENGTH_SHORT).show()
        } else {
            checkDataBase()
        }

    }

    private fun checkDataBase() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Marcas")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(IDMarca)) {
                    Toast.makeText(
                        this@AddMarcaActivity,
                        "Este ID já existe",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    addMarca()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addMarca() {

        // set up data
        val hashMap = HashMap<String, Any>()
        hashMap["ID"] = IDMarca.toInt()
        hashMap["Nome"] = nomeMarca
        hashMap["Imagem"] = ""


        // Save to DB
        val ref = FirebaseDatabase.getInstance().getReference("Marcas")
        ref.child(IDMarca.toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Marca Adicionada com sucesso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, BottomNavigationActivity::class.java))
                finish()
            }
    }


}