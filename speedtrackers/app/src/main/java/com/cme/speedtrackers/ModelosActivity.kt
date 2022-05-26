package com.cme.speedtrackers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.adapters.MarcasAdapter
import com.cme.speedtrackers.adapters.ModelosAdapter
import com.cme.speedtrackers.databinding.ActivityModelosBinding
import com.cme.speedtrackers.model.Marcas
import com.cme.speedtrackers.model.Modelos
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ModelosActivity : AppCompatActivity() {

    private lateinit var modelosRecyclerView: RecyclerView
    private lateinit var modelosList : ArrayList<Modelos>
    private lateinit var adapter : ModelosAdapter

    //book id
    public var marcaId = ""

    //view binding
    private lateinit var binding: ActivityModelosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModelosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gets intent
        marcaId = intent.getStringExtra("marcaId")!!
        Log.d("Daniel", marcaId)

        // Marcas
        modelosList = ArrayList()
        adapter = ModelosAdapter(this, modelosList)
        modelosRecyclerView = findViewById(R.id.modelosRecyclerView)
        modelosRecyclerView.layoutManager = GridLayoutManager(this, 2)
        modelosRecyclerView.adapter = adapter
        loadModelos()
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