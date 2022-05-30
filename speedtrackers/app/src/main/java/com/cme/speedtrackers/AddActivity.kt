package com.cme.speedtrackers

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cme.speedtrackers.adapters.EquipmentListAdapter
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.ActivityAddBinding
import com.cme.speedtrackers.dialogs.BottomSheetColorFragment
import com.cme.speedtrackers.dialogs.BottomSheetShoesFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


private lateinit var binding: ActivityAddBinding
private lateinit var datePickerDialog: DatePickerDialog
// GlobalClass
val compObj = GlobalClass.Companion

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()


        binding.clActivityHeader.visibility = View.GONE
        binding.rlBotoes.visibility = View.VISIBLE

        binding.btnCancel.visibility = View.VISIBLE
        binding.btnCancel.isEnabled = true
        binding.btnSave.isEnabled = true
        binding.btnSave.visibility = View.VISIBLE
        binding.btnCancel.setOnClickListener { onBackPressed() }
        binding.tvCancel.setOnClickListener { onBackPressed() }

        var cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR))
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH))
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))

        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        binding.etDate.text = sdf.format(cal.time)

        setUpDatePicker()

        binding.etDistaciaPercorrida.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textSample.setText("$p0 kilometros")
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etDuracao.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textSample2.setText("$p0 min")
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        //Get Values
        binding.btnSave.setOnClickListener {
            println("Tentei")
            if (checkConditions()){
                val dialog = BottomSheetShoesFragment()
                val ft = this.supportFragmentManager.beginTransaction()
                dialog.show(ft, ContentValues.TAG)
                compObj.currentDialog = dialog
                println("Existo")
            }
            else{
                println("Else")
            }
        }

        binding.tvAdd.setOnClickListener {
            if (checkConditions()){
                println("Pronto para submeter na base de dados")
                var nome = binding.etNomeAtividade.text.toString()
                var tipo = binding.etTipoAtividade.text.toString()
                var duracao = binding.etDuracao.text
                var data = binding.etDate.text.toString()
                var distancia = binding.etDistaciaPercorrida.text.toString().toDouble()
                var id = System.currentTimeMillis()
                var shoeID = compObj.activityShoe.Shoe_ID
                var userUID = FirebaseAuth.getInstance().uid
                var currentKM: Double? = compObj.activityShoe.KmTraveled
                var finalKM = currentKM?.plus(distancia)
                println("Objeto Atividade($nome, $tipo, $duracao, $data, $distancia, $id, $shoeID, $userUID)")
                FirebaseDatabase.getInstance().getReference("Sapatilhas").child(compObj.activityShoe.Shoe_ID.toString()).child("KmTraveled")
                    .setValue(finalKM)

                //CONTINUAR AQUI
            }
        }
    }

    private fun checkConditions() : Boolean {
        var nome = binding.etNomeAtividade.text.toString()
        var tipo = binding.etTipoAtividade.text.toString()
        var duracao = binding.etDuracao.text
        var data = binding.etDate.text.toString()
        var distancia = binding.etDistaciaPercorrida.text

        if (nome.isEmpty() and tipo.isEmpty() and duracao.isEmpty() and data.isEmpty() and distancia.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
        if (nome.isEmpty()) {
            Toast.makeText(this, "Insira um nome para a sua atividade", Toast.LENGTH_SHORT).show()
            return false
        }else if (tipo.isEmpty()){
            Toast.makeText(this, "Insira o tipo de atividade", Toast.LENGTH_SHORT).show()
            return false
        } else if (duracao.isEmpty()) {
            Toast.makeText(this, "Insira a duração da atividade", Toast.LENGTH_SHORT).show()
            return false
        } else if (distancia.isEmpty()) {
            Toast.makeText(this, "Insira a distância percorrida", Toast.LENGTH_SHORT).show()
            return false
        } else if (data.isEmpty()){
            Toast.makeText(this, "Insira a data da sua atividade", Toast.LENGTH_SHORT).show()
        }
        return true
    }


    private fun setUpDatePicker(){
        var cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd-MM-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
            binding.etDate.text = sdf.format(cal.time)
        }

        binding.etDate.setOnClickListener {
            binding.etDistaciaPercorrida.clearFocus()
            binding.etNomeAtividade.clearFocus()
            binding.etTipoAtividade.clearFocus()
            binding.etDuracao.clearFocus()

            DatePickerDialog(this@AddActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
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

        binding.btnSave.visibility = View.INVISIBLE
        binding.btnCancel.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        compObj.activityShoe.Shoe_ID = 0L
    }
}