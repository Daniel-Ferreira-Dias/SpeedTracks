package com.cme.speedtrackers

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cme.speedtrackers.databinding.ActivityAddMarca2Binding
import com.cme.speedtrackers.model.Marcas
import com.cme.speedtrackers.model.Shoes
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class AddMarcaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMarca2Binding

    //image uri
    private var imageUri: Uri? = null
    var uploadImageUrl = ""

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    var nomeMarca = ""
    var IDMarca = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMarca2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup progressdialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)


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

    private fun selectImage() {
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

    private fun uploadImage() {
        progressDialog.setMessage("Inserindo a Imagem...")
        progressDialog.show()
        val storageReference = FirebaseStorage.getInstance().getReference("modelos")
        storageReference.child("modelos/").putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                uploadImageUrl = "${uriTask.result}"
                addMarca()
            }
    }


    private fun checkConditions() {
        IDMarca = binding.etMarcaId.text.toString()
        nomeMarca = binding.etNomeMarca.text.toString()
        if (nomeMarca.isEmpty()) {
            Toast.makeText(this, "Insira o nome da Marca", Toast.LENGTH_SHORT).show()
        } else if (IDMarca.toString().isEmpty()) {
            Toast.makeText(this, "Insira o ID da Marca", Toast.LENGTH_SHORT).show()
        } else if (imageUri == null) {
            Toast.makeText(this, "Tem que inserir uma imagem", Toast.LENGTH_SHORT).show()
        } else {
            checkDataBase()
        }

    }

    private fun checkDataBase() {

        val dbRef = FirebaseDatabase.getInstance().getReference("Marcas")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(IDMarca)) {
                } else {
                    uploadImage()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addMarca() {
        progressDialog.setMessage("Inserindo na base de dados...")
        progressDialog.show()
        // set up data
        val hashMap = HashMap<String, Any>()
        hashMap["ID"] = IDMarca.toInt()
        hashMap["Nome"] = nomeMarca
        hashMap["Imagem"] = uploadImageUrl


        // Save to DB
        val ref = FirebaseDatabase.getInstance().getReference("Marcas")
        ref.child(IDMarca)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Marca Adicionada com sucesso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, BottomNavigationActivity::class.java))
                finish()
            }
    }


}