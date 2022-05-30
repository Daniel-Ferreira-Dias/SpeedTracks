package com.cme.speedtrackers.dialogs



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.FragmentModalDisplayActivityBinding
import com.cme.speedtrackers.model.Atividade


class ModalDisplayActivity(var atividade: Atividade) : DialogFragment() {
    private lateinit var binding: FragmentModalDisplayActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentModalDisplayActivityBinding.inflate(inflater, container, false)

        //TYPE YOUR CODE
        binding.tvDia.text = atividade.Data
        binding.tvExercicio.text = atividade.TipoExercicio
        binding.tvNomeAtividade.text = atividade.NomeAtividade

        var duracao: String = "${atividade.Duracao}min"
        var distancia: String = "${atividade.DistanciaPercorrida} km"
        binding.tvDuracao.text = duracao
        binding.tvDistancia.text = distancia

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


    private fun setupClickListeners(view: View) {
        binding.ibGoBack.setOnClickListener {
            dismiss()
        }

    }
}