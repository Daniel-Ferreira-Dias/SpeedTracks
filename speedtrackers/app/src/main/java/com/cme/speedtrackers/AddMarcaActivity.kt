package com.cme.speedtrackers

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cme.speedtrackers.databinding.ActivityAddMarca2Binding


class AddMarcaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMarca2Binding

    var nomeMarca = ""
    var IDMarca = 0

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


    private fun checkDataBase(){

    }


    private fun checkConditions(){
        IDMarca = binding.etMarcaId.text.toString().toInt()
        nomeMarca = binding.etNomeMarca.text.toString()

        if (nomeMarca.isEmpty()){
            Toast.makeText(this, "Insira o nome da Marca", Toast.LENGTH_SHORT).show()
        }else if (IDMarca.toString().isEmpty()){
            Toast.makeText(this, "Insira o ID da Marca", Toast.LENGTH_SHORT).show()
        }

    }
}