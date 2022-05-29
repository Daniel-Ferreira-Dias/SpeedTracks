package com.cme.speedtrackers

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cme.speedtrackers.databinding.ActivityAddBinding
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


private lateinit var binding: ActivityAddBinding
private lateinit var datePickerDialog: DatePickerDialog

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.btnCancel.visibility = View.VISIBLE
        binding.btnCancel.isEnabled = true
        binding.btnSave.isEnabled = true
        binding.btnSave.visibility = View.VISIBLE
        binding.btnCancel.setOnClickListener { onBackPressed() }

        setUpDatePicker()

        binding.etDistaciaPercorrida.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textSample.setText("$p0 Kilometers")
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
            var nomeAtividade: String = binding.etNomeAtividade.text.toString()
            var tipoAtividade: String = binding.etTipoAtividade.text.toString()
            var duracaoAtividade = binding.etDuracao.text
            var distanciaAtividade = binding.etDistaciaPercorrida.text
            var dataAtividade = binding.etDate.text
            printVariables(nomeAtividade, tipoAtividade, duracaoAtividade, distanciaAtividade, dataAtividade)
        }


    }

    private fun printVariables(nomeAtividade: String, tipoAtividade: String, duracaoAtividade: Editable?, distanciaAtividade: Editable?, dataAtividade: CharSequence?) {

        println("$nomeAtividade, $tipoAtividade, $duracaoAtividade, $distanciaAtividade, $dataAtividade")
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
        binding.etDistaciaPercorrida.setOnClickListener {
            binding.etDistaciaPercorrida.isCursorVisible = true
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

}