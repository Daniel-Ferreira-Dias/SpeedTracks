package com.cme.speedtrackers.dialogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.ModalSimpleShowViewBinding
import com.cme.speedtrackers.model.Shoes
import java.lang.Exception


class ModalSimpleShoeView(var shoe: Shoes) : DialogFragment() {
    private lateinit var binding: ModalSimpleShowViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ModalSimpleShowViewBinding.inflate(inflater, container, false)

        //TYPE YOUR CODE
        loadImage(binding.ivImagem, shoe.ImageURL)
        loadImage(binding.ivCor, shoe.ColorURL)
        binding.tvName.text = shoe.Shoe_Nome
        binding.tvMarca.text = shoe.Brand_Nome
        binding.tvModelo.text = shoe.Model_Nome
        binding.tvData.text = getDataStringFormatted(shoe.FirstUsage.toString())
        binding.tvTamanho.text = shoe.Shoe_Size.toString()
        binding.tvKm.text = shoe.KmTraveled.toString()
        var restantes =  600 - shoe.KmTraveled.toString().toDouble()

        if (restantes.compareTo(600) <= 0){
            binding.tvKmRestantes.text = restantes.toString()
        }
        else if(restantes.compareTo(0) <= 0){
            binding.tvKmRestantes.text = "Limite atingido"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun loadImage(imageV: ImageView, imageURL: String) {
        try {
            Glide.with(this).load(imageURL)
                .placeholder(R.drawable.progress_animation)
                .into(imageV)
        } catch (e: Exception) {
        }
    }


    private fun setupClickListeners(view: View) {
        binding.ibGoBack.setOnClickListener {
            dismiss()
        }
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
}