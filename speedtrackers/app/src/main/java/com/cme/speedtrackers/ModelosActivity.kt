package com.cme.speedtrackers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.adapters.ModelosAdapter
import com.cme.speedtrackers.classes.GlobalClass
import com.cme.speedtrackers.databinding.ActivityModelosBinding
import com.cme.speedtrackers.model.Modelos
import com.cme.speedtrackers.model.Shoes
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

        //Type your code
        binding.tvNotFound.visibility = View.GONE
        binding.modelosRecyclerView.hasFixedSize()

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

        // SearchView
        binding.searchView.clearFocus()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                return true
            }
        })
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
                adapter.setFilteredList(modelosList)
                if(modelosList.isNotEmpty()){
                    modelosList.reverse()
                    binding.modelosRecyclerView.adapter = adapter
                    binding.modelosRecyclerView.visibility = View.VISIBLE
                    binding.tvCarregando.visibility = View.GONE
                    binding.tvNotFound.visibility = View.GONE
                }
                else {
                    binding.tvCarregando.visibility = View.GONE
                    binding.tvNotFound.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        if (modelosList.isEmpty()){
            binding.tvCarregando.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
        }
    }


    private  fun filter(e: String) {
        //Declare the array list that holds the filtered values
        var filteredItem = java.util.ArrayList<Modelos>()
        // loop through the array list to obtain the required value
        for (item in modelosList) {

            if (item.Nome_Modelo.toString().lowercase().filterNot { it.isWhitespace() }.contains(e.lowercase().filterNot { it.isWhitespace() })) {
                filteredItem.add(item)
            }
        }
        println("Lista - $filteredItem")
        println("Para query $e - ${filteredItem.size}")
        if (filteredItem.isEmpty()){
            binding.modelosRecyclerView.clearFocus()
            binding.modelosRecyclerView.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
        }
        else{
            adapter.setFilteredList(filteredItem)
            binding.modelosRecyclerView.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        }
    }
}