package com.cme.speedtrackers.dialogs

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.R
import com.cme.speedtrackers.adapters.CoresAdapter
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.model.Cores
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.*

class BottomSheetColorFragment : BottomSheetDialogFragment {

    private lateinit var mDbRef: DatabaseReference

    private lateinit var coresRecyclerView: RecyclerView
    private lateinit var coresList : ArrayList<Cores>
    private lateinit var coreAdapter : CoresAdapter
    private lateinit var textView: TextView

    val compObj = GlobalClass.Companion

    var modeloId = ""

    var modeloId_GlobalClass = ""
    var modeloName_GlobalClass = ""
    var brandName_GlobalClass = ""
    var brandId_GlobalClass = ""

    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_colour_fragment, container, false)

        modeloId_GlobalClass = compObj.Model_ID
        modeloName_GlobalClass = compObj.Model_Name
        brandName_GlobalClass = compObj.Brand_Name
        brandId_GlobalClass = compObj.Brand_ID

        // Marcas
        coresList = ArrayList()
        coreAdapter = CoresAdapter(requireContext(), coresList)
        coresRecyclerView = view.findViewById(R.id.colorRecycler)
        coresRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        coresRecyclerView.adapter = coreAdapter

        val data = arguments
        modeloId = data!!.get("modeloId").toString()
        Log.d("Check", modeloId)

        textView = view.findViewById(R.id.textView)
        textView.setOnClickListener {
            Toast.makeText(context, "${modeloId}", Toast.LENGTH_SHORT).show()
        }

        // Load cores
        loadCores()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun loadCores(){
        val ref = FirebaseDatabase.getInstance().getReference("Cores")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                coresList.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(Cores::class.java)
                    coresList.add(model as Cores)
                }
                coresRecyclerView.adapter = coreAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}