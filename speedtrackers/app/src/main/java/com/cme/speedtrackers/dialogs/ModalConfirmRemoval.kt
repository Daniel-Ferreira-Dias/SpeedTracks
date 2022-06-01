package com.cme.speedtrackers.dialogs



import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.cme.speedtrackers.EquipmentViewActivity
import com.cme.speedtrackers.R
import com.cme.speedtrackers.databinding.ModalConfirmRemovalBinding
import com.cme.speedtrackers.model.Shoes
import com.google.firebase.database.FirebaseDatabase


class ModalConfirmRemoval(var list: ArrayList<Shoes>, var position: Int, var atividade: EquipmentViewActivity) : DialogFragment() {
    private lateinit var binding: ModalConfirmRemovalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ModalConfirmRemovalBinding.inflate(inflater, container, false)

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

            Toast.makeText(context, "Equipamento removido", Toast.LENGTH_SHORT).show()
            FirebaseDatabase.getInstance().getReference("Sapatilhas").child(list[position].Shoe_ID.toString()).child("EquipamentoAtivo")
                .setValue(false)

            list.remove(list[position])
            if (list.isEmpty()){
                dismiss()
                atividade?.finish()
            }
            else if (position == 0 && list.isNotEmpty()){
                position = 0
            }
            else if (position.compareTo(list.size) <= 0 && position != 0 ) {
                position -= 1
            }
            else if(position == 0 && list.size == 1){
                position = 0
            }
            else {
                position += 1
            }
            dismiss()
        }
        binding.ibGoBack.setOnClickListener { dismiss() }
        binding.btnCancel.setOnClickListener { dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (list.size != 0){
            atividade.changeViews(position, list)
        }
    }
}