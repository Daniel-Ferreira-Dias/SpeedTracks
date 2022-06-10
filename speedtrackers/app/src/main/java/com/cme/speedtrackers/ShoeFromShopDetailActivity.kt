package com.cme.speedtrackers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.databinding.ActivityShoeFromShopDetailBinding
import com.cme.speedtrackers.dialogs.CustomCommentFragment
import com.example.bookapplicationv1.classes.ModelComment
import com.example.bookapplicationv1.fragments.adapters.AdapterComment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


class ShoeFromShopDetailActivity : AppCompatActivity() {

    var modeloId = ""
    var marcaId = ""

    //view binding
    private lateinit var binding: ActivityShoeFromShopDetailBinding

    //comment arraylist
    private lateinit var commentArrayList: ArrayList<ModelComment>
    //adaptar for recycler
    private lateinit var adapterComment: AdapterComment
    //recycler
    private lateinit var commentRecyclerView: RecyclerView

    // firebase auth
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoeFromShopDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tamanhos = resources.getStringArray(R.array.Tamanho)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, tamanhos)
        binding.AutoCompleteText.setAdapter(arrayAdapter)

        binding.AutoCompleteText.inputType = 0

        //Gets intent

        modeloId = intent.getStringExtra("List")!!
        marcaId = intent.getStringExtra("Marca")!!
        Log.d("ID" , modeloId)

        //Load Shop Info
        loadShopInfo()

        // comments
        showComments()
        //arraylist
        commentArrayList = ArrayList()
        adapterComment = AdapterComment(this, commentArrayList)
        commentRecyclerView = findViewById(R.id.commentsRV)
        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentRecyclerView.adapter = adapterComment

        //Exit
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // to be able to add a comment
        binding.commentBtn.setOnClickListener {
            if (mAuth.currentUser == null){
                Toast.makeText(this, "Tem que fazer o login para efetuar um comentário", Toast.LENGTH_SHORT).show()
            }else{
                val bundle = Bundle()
                bundle.putString("modeloId", modeloId)
                bundle.putString("marcaId", marcaId)
                val dialog = CustomCommentFragment()
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "Comment")
            }
        }

        binding.btnCart.setOnClickListener {
            Toast.makeText(this, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show()
        }

        binding.btnComprar.setOnClickListener {
            Toast.makeText(this, "Sapatilha comprada com sucesso", Toast.LENGTH_SHORT).show()
        }


        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val tamanhos = resources.getStringArray(R.array.Tamanho)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, tamanhos)
        binding.AutoCompleteText.setAdapter(arrayAdapter)
    }

    private fun loadShopInfo(){
        var ref = FirebaseDatabase.getInstance().getReference("Shop").child("Modelos").child(modeloId)
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // get data
                val Model_Name = "${snapshot.child("Nome_Modelo").value}"
                val Price = "${snapshot.child("Preço").value}"
                val Imagem = "${snapshot.child("Imagem_Modelo").value}"
                val Desc = "${snapshot.child("Descrição").value}"

                binding.bookDescricao.text = Desc
                binding.shoeName.text = Model_Name
                binding.shoePriceTag.text = Price


                try {
                    Glide.with(this@ShoeFromShopDetailActivity)
                        .load(Imagem)
                        .placeholder(R.drawable.progress_animation)
                        .into(binding.imagemModelo)
                } catch (e: Exception) {
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // ---- Comment Functions ---- //

    private fun showComments() {
        //db path to load comments
        val ref = FirebaseDatabase.getInstance().getReference("Shop").child("Modelos")
        ref.child(modeloId).child("Comments")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    commentArrayList.clear()
                    for (ds in snapshot.children){
                        val model = ds.getValue(ModelComment::class.java)
                        commentArrayList.add(model!!)
                    }
                    binding.commentsRV.adapter = adapterComment
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}