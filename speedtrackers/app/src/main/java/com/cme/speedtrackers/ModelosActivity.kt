package com.cme.speedtrackers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.adapters.ModelosAdapter
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.ActivityModelosBinding
import com.cme.speedtrackers.model.Modelos
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ModelosActivity : AppCompatActivity() {

    private lateinit var modelosRecyclerView: RecyclerView
    private lateinit var modelosList : ArrayList<Modelos>
    private lateinit var adapter : ModelosAdapter

    //Marca id
    public var marcaId = ""
    public var marcaNome = ""

    public var marcaId_Global = ""
    public var marcaNome_Global = ""

    //view binding
    private lateinit var binding: ActivityModelosBinding

    // Global
    val compObj = GlobalClass.Companion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModelosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Gets intent
        marcaId = intent.getStringExtra("marcaId")!!

        //Global
        marcaId_Global = compObj.Brand_ID
        marcaNome_Global = compObj.Brand_Name

        // Marcas
        modelosList = ArrayList()
        adapter = ModelosAdapter(this, modelosList)
        modelosRecyclerView = findViewById(R.id.modelosRecyclerView)
        modelosRecyclerView.layoutManager = GridLayoutManager(this, 2)
        modelosRecyclerView.adapter = adapter
        loadModelos()


        binding.backButton.setOnClickListener {
            onBackPressed()
        }


    }


    private fun loadModelos(){
        val ref = FirebaseDatabase.getInstance().getReference("Marcas")
        ref.child(marcaId).child("Modelos")
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                modelosList.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(Modelos::class.java)

                    modelosList.add(model!!)
                }
                modelosRecyclerView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}