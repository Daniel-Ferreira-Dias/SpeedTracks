package com.cme.speedtrackers.dialogs



import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.cme.speedtrackers.EquipmentViewActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.compObj
import com.cme.speedtrackers.databinding.ModalChangeNameBinding
import com.cme.speedtrackers.model.Atividade
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList


class ModalChangeName(var shoe: Shoes, var list: ArrayList<Shoes>, var position: Int, var atividade: EquipmentViewActivity) : DialogFragment() {
    private lateinit var binding: ModalChangeNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ModalChangeNameBinding.inflate(inflater, container, false)

        //TYPE YOUR CODE


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
        binding.btnSave.setOnClickListener {
            var novoNome = binding.etNovoNome.text
            if (novoNome.isNotEmpty()){
                FirebaseDatabase.getInstance().getReference("Sapatilhas").child(shoe.Shoe_ID.toString()).child("Shoe_Nome")
                    .setValue(novoNome.toString())
                list[position].Shoe_Nome = novoNome.toString()
                var view: Button? = activity?.findViewById<Button>(R.id.btn_nome_modelo)
                view?.text = novoNome.toString()
                Toast.makeText(context, "Nome foi alterado", Toast.LENGTH_SHORT).show()
                dismiss()
                view?.clearFocus()
            }
            else{
                Toast.makeText(context, "Dê um novo nome ao seu equipamento", Toast.LENGTH_SHORT).show()
            }
        }
        binding.ibGoBack.setOnClickListener { dismiss() }
        binding.btnCancel.setOnClickListener { dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        atividade.changeViews(position, list)
    }
}