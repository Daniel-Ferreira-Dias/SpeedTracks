package com.cme.speedtrackers.dialogs

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.cme.speedtrackers.*
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.ModalCreateActivityBinding
import com.cme.speedtrackers.model.Atividade
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class ModalCreateActivity(var shoe: Shoes) : DialogFragment() {
    private lateinit var binding: ModalCreateActivityBinding
    // GlobalClass
    val compObj = GlobalClass.Companion

    // Variables for Activity creation
    var nome = ""
    var tipo = ""
    var duracao = 0
    var data = ""
    var distancia = 0.0
    var id = 0L
    var shoeID = 0L
    var userUID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ModalCreateActivityBinding.inflate(inflater, container, false)

        //TYPE YOUR CODE
        binding.rlBotoes.visibility = View.VISIBLE
        binding.btnCancel.visibility = View.VISIBLE
        binding.btnCancel.isEnabled = true
        binding.btnSave.isEnabled = true
        binding.btnSave.visibility = View.VISIBLE
        binding.btnCancel.setOnClickListener { dismiss() }


        var cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR))
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH))
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))

        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        data = sdf.format(cal.time)
        binding.etDate.text = getDataStringFormatted(sdf.format(cal.time))

        setUpDatePicker()

        binding.etDistaciaPercorrida.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textSample.text = "$p0 kilometros"
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etDuracao.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.textSample2.text = "$p0 min"
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        //Get Values
        binding.btnSave.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                if (checkConditions()){
                    println("Pronto para submeter na base de dados")
                    nome = binding.etNomeAtividade.text.toString()
                    tipo = binding.etTipoAtividade.text.toString()
                    duracao = binding.etDuracao.text.toString().toInt()
                    distancia = binding.etDistaciaPercorrida.text.toString().toDouble()
                    id = System.currentTimeMillis()
                    shoeID = shoe.Shoe_ID
                    userUID = FirebaseAuth.getInstance().uid.toString()
                    var currentKM: Double? = compObj.activityShoe.KmTraveled
                    var finalKM = currentKM?.plus(distancia)
                    println("Objeto Atividade($nome, $tipo, $duracao, $data, $distancia, ${id}, $shoeID, $userUID)")
                    FirebaseDatabase.getInstance().getReference("Sapatilhas").child(shoe.Shoe_ID.toString()).child("KmTraveled")
                        .setValue(finalKM) // Atualizar kilometragem
                    addActivity()
                }

            } else {
                Toast.makeText(
                    context,
                    "É necessário estar numa sessão válida!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.ibGoBack.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
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

    private fun addActivity() {
        // set up data
        var ativi = Atividade()
        val hashMap = HashMap<String, Any>()
        hashMap["NomeAtividade"] = nome
        hashMap["TipoExercicio"] = tipo
        hashMap["DistanciaPercorrida"] = distancia
        hashMap["Duracao"] = duracao
        hashMap["Data"] = data
        hashMap["Shoe_ID"] = shoeID
        hashMap["ID"] = id
        hashMap["User_UID"] = userUID
        hashMap["MapURL"] = ativi.MapURL
        hashMap["ImageURL"] = shoe.ImageURL

        // Save to DB
        val ref = FirebaseDatabase.getInstance().getReference("Atividades")
        ref.child(id.toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(context, "Atividade adicionada com sucesso", Toast.LENGTH_SHORT).show()
                dismiss()
            }.addOnCanceledListener {
                Toast.makeText(context, "Não foi possível criar Atividade. Tente Novamente!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
    }

    private fun setUpDatePicker(){
        var cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd-MM-yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
            data = sdf.format(cal.time)
            binding.etDate.text = getDataStringFormatted(sdf.format(cal.time))
        }

        binding.etDate.setOnClickListener {
            binding.etDistaciaPercorrida.clearFocus()
            binding.etNomeAtividade.clearFocus()
            binding.etTipoAtividade.clearFocus()
            binding.etDuracao.clearFocus()

            DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun checkConditions() : Boolean {
        var nome = binding.etNomeAtividade.text.toString()
        var tipo = binding.etTipoAtividade.text.toString()
        var duracao = binding.etDuracao.text
        var distancia = binding.etDistaciaPercorrida.text

        if (nome.isEmpty() and tipo.isEmpty() and duracao.isEmpty() and distancia.isEmpty()){
            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
        if (nome.isEmpty()) {
            Toast.makeText(context, "Insira um nome para a sua atividade", Toast.LENGTH_SHORT).show()
            return false
        }else if (tipo.isEmpty()){
            Toast.makeText(context, "Insira o tipo de atividade", Toast.LENGTH_SHORT).show()
            return false
        } else if (duracao.isEmpty()) {
            Toast.makeText(context, "Insira a duração da atividade", Toast.LENGTH_SHORT).show()
            return false
        } else if (distancia.isEmpty()) {
            Toast.makeText(context, "Insira a distância percorrida", Toast.LENGTH_SHORT).show()
            return false
        } else if (data.isEmpty()){
            Toast.makeText(context, "Insira a data da sua atividade", Toast.LENGTH_SHORT).show()
        }
        return true
    }

}