package com.cme.speedtrackers.dialogs

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.cme.speedtrackers.R
import com.cme.speedtrackers.compObj
import com.cme.speedtrackers.databinding.FragmentDialogCommentBinding
import com.cme.speedtrackers.databinding.FragmentEquipmentHistoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class CustomCommentFragment : BottomSheetDialogFragment() {

    var modeloId = ""
    var marcaId = ""
    //comment
    private var comment = ""
    private var modeloRating = ""

    //rating
    private var totalRating = 0
    private var currentRating = 0F
    private var finalrating = 0F

    // firebase auth
    private val mAuth = FirebaseAuth.getInstance()
    private val user: FirebaseUser? = mAuth.currentUser

    lateinit var binding: FragmentDialogCommentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogCommentBinding.inflate(layoutInflater)

        val data = arguments
        modeloId = data!!.get("modeloId").toString()
        marcaId = data.get("marcaId").toString()

        loadShoeCurrentRating()
        loadUser()


        binding.submitComment.setOnClickListener {
            comment = binding.reviewTextField.text.toString().trim()
            modeloRating = binding.ratingStars.rating.toString()

            if (comment.isEmpty() || modeloRating.isEmpty()){
                Toast.makeText(requireContext(), "Insira os campos necessários", Toast.LENGTH_SHORT).show()
            }else{
                addComment()
            }
        }

        return binding.root
    }

    private fun loadUser() {
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(user!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profileImage = "${snapshot.child("userProfilePic").value}"
                    //set image
                    try {
                        Glide.with(this@CustomCommentFragment).load(profileImage)
                            .placeholder(R.drawable.ic_user_placeholder).into(binding.profilePic)
                    } catch (e: Exception) {
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun loadShoeCurrentRating() {
        val ref = FirebaseDatabase.getInstance().getReference("Marcas").child(marcaId).child("Modelos").child(modeloId).child("Reviews")
        ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentRating_ = "${snapshot.child("atualreviews").value}".toFloat()
                    val totalRating_ = "${snapshot.child("totalreviews").value}".toInt()

                    totalRating = totalRating_
                    currentRating = currentRating_

                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun addComment() {

        val timestamp = "${System.currentTimeMillis()}"

        // set up data to add in db for comment
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["modelId"] = "$modeloId"
        hashMap["timestamp"] = "$timestamp"
        hashMap["comment"] = "$comment"
        hashMap["uid"] = "${mAuth.uid}"
        hashMap["rating"] = "$modeloRating"
        hashMap["userPhoto"] = compObj.currentUser.userProfilePic.toString()


        val mref = FirebaseDatabase.getInstance().getReference("Comentários")
        mref.child(timestamp)
            .setValue(hashMap)

        // sum one totalrating and current rating and update in books
        totalRating += 1
        currentRating += modeloRating.toFloat()
        finalrating = currentRating/totalRating

        val secref = FirebaseDatabase.getInstance().getReference("Marcas").child(marcaId).child("Modelos").child(modeloId).child("Reviews")
        secref.child("atualreviews").setValue(currentRating)
        secref.child("totalreviews").setValue(totalRating)
        secref.child("finalrating").setValue(finalrating)

        // Db path to add into it
        val ref = FirebaseDatabase.getInstance().getReference("Shop").child("Modelos")
        ref.child(modeloId).child("Comments").child(timestamp)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Comentário adicionado!", Toast.LENGTH_SHORT).show()
                dismiss()
            }.addOnFailureListener {e ->
                Toast.makeText(requireContext(), "Não foi possivel adicionar comentário devido a ${e.message}", Toast.LENGTH_SHORT).show()
                dismiss()
            }
    }

}