package com.cme.speedtrackers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.adapters.MarcasAdapter
import com.cme.speedtrackers.model.Marcas
import com.google.firebase.database.*

class MarcasActivity : AppCompatActivity() {

    private lateinit var marcasRecyclerView : RecyclerView
    private lateinit var marcasList : ArrayList<Marcas>
    private lateinit var adapter : MarcasAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_marca)



        // Marcas
        marcasList = ArrayList()
        adapter = MarcasAdapter(this, marcasList)
        marcasRecyclerView = findViewById(R.id.marcaRecyclerView)
        marcasRecyclerView.layoutManager = GridLayoutManager(this, 2)
        marcasRecyclerView.adapter = adapter
        loadMarcas()

    }

    private fun loadMarcas(){
        val ref = FirebaseDatabase.getInstance().getReference("Marcas")
            ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                marcasList.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(Marcas::class.java)

                    marcasList.add(model!!)
                }
                marcasRecyclerView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}