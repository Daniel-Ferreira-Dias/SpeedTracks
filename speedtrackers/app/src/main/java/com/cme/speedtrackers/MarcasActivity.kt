package com.cme.speedtrackers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cme.speedtrackers.adapters.MarcasAdapter
import com.cme.speedtrackers.databinding.ActivityAddMarcaBinding
import com.cme.speedtrackers.databinding.ActivityModelosBinding
import com.cme.speedtrackers.model.Marcas
import com.cme.speedtrackers.model.Modelos
import com.google.firebase.database.*

class MarcasActivity : AppCompatActivity() {

    private lateinit var marcasRecyclerView : RecyclerView
    private lateinit var marcasList : ArrayList<Marcas>
    private lateinit var adapter : MarcasAdapter

    private lateinit var backButton: ImageView

    //view binding
    private lateinit var binding: ActivityAddMarcaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMarcaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Type your code
        binding.tvNotFound.visibility = View.GONE
        binding.marcaRecyclerView.hasFixedSize()

        // Marcas
        marcasList = ArrayList()
        adapter = MarcasAdapter(this, marcasList)
        marcasRecyclerView = findViewById(R.id.marcaRecyclerView)
        marcasRecyclerView.layoutManager = GridLayoutManager(this, 2)
        marcasRecyclerView.adapter = adapter
        loadMarcas()

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
                adapter.setFilteredList(marcasList)
                if(marcasList.isNotEmpty()){
                    marcasList.reverse()
                    binding.marcaRecyclerView.adapter = adapter
                    binding.marcaRecyclerView.visibility = View.VISIBLE
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
        if (marcasList.isEmpty()){
            binding.tvCarregando.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
        }
    }

    private  fun filter(e: String) {
        //Declare the array list that holds the filtered values
        var filteredItem = java.util.ArrayList<Marcas>()
        // loop through the array list to obtain the required value
        for (item in marcasList) {

            if (item.Nome.lowercase().filterNot { it.isWhitespace() }.contains(e.lowercase().filterNot { it.isWhitespace() })) {
                filteredItem.add(item)
            }
        }
        println("Lista - $filteredItem")
        println("Para query $e - ${filteredItem.size}")
        if (filteredItem.isEmpty()){
            binding.marcaRecyclerView.clearFocus()
            binding.marcaRecyclerView.visibility = View.GONE
            binding.tvNotFound.visibility = View.VISIBLE
        }
        else{
            adapter.setFilteredList(filteredItem)
            binding.marcaRecyclerView.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.GONE
        }
    }

}