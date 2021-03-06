package com.cme.speedtrackers

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.cme.speedtrackers.databinding.ActivityAddMarca2Binding
import com.cme.speedtrackers.databinding.ActivityAddModelBinding
import com.cme.speedtrackers.model.Marcas
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.net.URI

class AddModelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddModelBinding

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    //image uri
    private var imageUri: Uri? = null

    var nomeModelo = ""
    var IDMarca = ""
    var IDModel = ""

    var uploadImageUrl = ""
    val MarcasList = ArrayList<Int>()

    override fun onResume() {
        super.onResume()
        loadMarcasId()
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, MarcasList)
        binding.etMarcaId.setAdapter(arrayAdapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddModelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etMarcaId.inputType = 0

        //setup progressdialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.etMarcaId.addTextChangedListener {
            IDMarca = binding.etMarcaId.text.toString()
        }

        binding.ibAddImage.setOnClickListener {
            selectImage()
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        binding.ibGoBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            checkConditions()
        }
    }

    private fun loadMarcasId(){
        val mref = FirebaseDatabase.getInstance().getReference("Marcas")
        mref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    val model = ds.getValue(Marcas::class.java)
                    MarcasList.add(model!!.ID.toInt())
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun uploadImage(){
        progressDialog.setMessage("Inserindo a Imagem...")
        progressDialog.show()
        val storageReference = FirebaseStorage.getInstance().getReference("modelos")
        storageReference.child("modelos/").putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                uploadImageUrl = "${uriTask.result}"
                addModelo()
            }
    }

    private fun selectImage(){
        // pick image from garely
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    // handle for gallery
    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imageUri = data!!.data
            } else {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private fun checkConditions() {
        IDMarca = binding.etMarcaId.text.toString()
        nomeModelo = binding.etNomeModelo.text.toString()
        IDModel = binding.etModelId.text.toString()

        if (nomeModelo.isEmpty()) {
            Toast.makeText(this, "Insira o nome da Marca", Toast.LENGTH_SHORT).show()
        } else if (IDMarca.isEmpty() || IDMarca == "Marca ID") {
            Toast.makeText(this, "Insira o ID da Marca", Toast.LENGTH_SHORT).show()
        } else if (IDModel.isEmpty()) {
            Toast.makeText(this, "Insira o ID do Modelo", Toast.LENGTH_SHORT).show()
        } else if (imageUri == null){
            Toast.makeText(this, "Tem que inserir uma imagem", Toast.LENGTH_SHORT).show()
        } else {
            checkDataBase()
        }

    }

    private fun checkDataBase() {
        val dbRef = FirebaseDatabase.getInstance().getReference("Marcas").child("Modelos")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(IDModel)) {
                } else {

                    uploadImage()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addModelo() {
        progressDialog.setMessage("Inserindo na base de dados...")
        progressDialog.show()
        // set up data
        val hashMap = HashMap<String, Any>()
        hashMap["ID_Modelo"] = IDModel.toInt()
        hashMap["Nome_Modelo"] = nomeModelo
        hashMap["Imagem_Modelo"] = uploadImageUrl


        // Save to DB
        val ref = FirebaseDatabase.getInstance().getReference("Marcas")
        ref.child(IDMarca.toString()).child("Modelos").child(IDModel)
            .setValue(hashMap)


        val secondhashMap = HashMap<String, Any>()
        secondhashMap["atualreviews"] = 0
        secondhashMap["finalrating"] = 0
        secondhashMap["totalreviews"] = 0

        val mref = FirebaseDatabase.getInstance().getReference("Marcas")
        mref.child(IDMarca.toString()).child("Modelos").child(IDModel).child("Reviews")
            .setValue(secondhashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Marca Adicionada com sucesso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, BottomNavigationActivity::class.java))
                finish()
            }
    }
}