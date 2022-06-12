package com.cme.speedtrackers


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cme.speedtrackers.databinding.ActivityProfileViewBinding
import com.example.bookapplicationv1.classes.User
import com.google.firebase.auth.FirebaseAuth
import com.cme.speedtrackers.classes.GlideLoader
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException


private lateinit var binding: ActivityProfileViewBinding
const val PICK_IMAGE_REQUEST_CODE = 1
const val READ_STORAGE_PERMISSION_CODE = 2



class ProfileViewActivity : AppCompatActivity() {

    private lateinit var mUserDetails: User
    private var mSelectedImageUri: Uri? = null
    private var mUserProfileImageURL: String = ""
    private var userUID: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        userUID = FirebaseAuth.getInstance().uid.toString()
        mUserDetails = compObj.currentUser
        GlideLoader(this).loadUserPicture(mUserDetails.userProfilePic.toString(), binding.ivUserPhoto)
        binding.ivUserPhoto.isEnabled = false
        binding.ivUserPhoto.isClickable = false

        //LOAD DATA
        binding.etEmail.setText(mUserDetails.userEmail)
        binding.etUsername.setText(mUserDetails.userName)
        if (mUserDetails.userFirstName != null){
            binding.etFirstName.setText(mUserDetails.userFirstName)
        }
        if (mUserDetails.userLastName != null){
            binding.etLastName.setText(mUserDetails.userLastName)
        }



        binding.ivUserPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ){
                showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_PERMISSION_CODE)
            }
        }

        binding.btnEdit.setOnClickListener {
            //binding.btnEdit.setImageResource(R.drawable.edit_pressed)
            @Suppress("DEPRECATION")
            Handler().postDelayed(
                {
                    binding.btnSave.visibility = View.VISIBLE
                    binding.btnCancel.visibility = View.VISIBLE
                    binding.btnEdit.visibility = View.INVISIBLE
                    //binding.btnEdit.setImageResource(R.drawable.edit)
                },
                100
            )

            binding.etUsername.isEnabled = false
            binding.etFirstName.isEnabled = true
            binding.etLastName.isEnabled = true
            binding.etEmail.isEnabled = false
            binding.ivUserPhoto.isEnabled = true
            binding.ivUserPhoto.isClickable = true
        }

        binding.btnCancel.setOnClickListener {

            @Suppress("DEPRECATION")
            Handler().postDelayed({
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnSave.visibility = View.INVISIBLE
                binding.btnCancel.visibility = View.INVISIBLE
            },
                100
            )
            binding.etUsername.isEnabled = false
            binding.etFirstName.isEnabled = false
            binding.etLastName.isEnabled = false
            binding.etEmail.isEnabled = false
            binding.ivUserPhoto.isEnabled = false
            binding.ivUserPhoto.isClickable = false
            if (compObj.currentUser.userFirstName != null || compObj.currentUser.userFirstName != ""){
                binding.etFirstName.setText(compObj.currentUser.userFirstName)
            }
            else{
                binding.etFirstName.text.clear()
            }
            if (compObj.currentUser.userLastName != null || compObj.currentUser.userLastName != ""){
                binding.etLastName.setText(compObj.currentUser.userLastName)
            }
            else{
                binding.etLastName.text.clear()
            }


        }

        binding.btnSave.setOnClickListener {
            if (mSelectedImageUri != null){
                uploadImageToCloudStorage(this, mSelectedImageUri)
                updateUserProfileDetails()
                @Suppress("DEPRECATION")
                Handler().postDelayed({
                    binding.btnEdit.visibility = View.VISIBLE
                    binding.btnSave.visibility = View.INVISIBLE
                    binding.btnCancel.visibility = View.INVISIBLE
                },
                    100
                )
                binding.etUsername.isEnabled = false
                binding.etFirstName.isEnabled = false
                binding.etLastName.isEnabled = false
                binding.etEmail.isEnabled = false
                binding.ivUserPhoto.isEnabled = false
                binding.ivUserPhoto.isClickable = false
            }

            else {
                updateUserProfileDetails()
                @Suppress("DEPRECATION")
                Handler().postDelayed({
                    binding.btnEdit.visibility = View.VISIBLE
                    binding.btnSave.visibility = View.INVISIBLE
                    binding.btnCancel.visibility = View.INVISIBLE
                },
                    100
                )
                binding.etUsername.isEnabled = false
                binding.etFirstName.isEnabled = false
                binding.etLastName.isEnabled = false
                binding.etEmail.isEnabled = false
                binding.ivUserPhoto.isEnabled = false
                binding.ivUserPhoto.isClickable = false
            }
        }
    }
    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String, Any>()
        val firstName = binding.etFirstName.text.toString().trim(){ it <= ' '}
        val lastName = binding.etLastName.text.toString().trim(){ it <= ' '}

        if (lastName != "" || lastName != null){
            userHashMap["userLastName"] = lastName
        }
        if (lastName != "" || lastName != null){
            userHashMap["userFirstName"] = firstName
        }

        userHashMap["userName"] = compObj.currentUser.userName.toString()
        userHashMap["userEmail"] = compObj.currentUser.userEmail.toString()
        userHashMap["userType"] = compObj.currentUser.userType.toString()
        userHashMap["userTimestamp"] = compObj.currentUser.userTimestamp.toString().toLong()
        userHashMap["userVerified"] = compObj.currentUser.userVerified.toString().toBoolean()
        userHashMap["userUID"] = compObj.currentUser.userUID.toString()
        userHashMap["userProfilePic"] = compObj.currentUser.userProfilePic.toString()
        userHashMap["admin"] = compObj.currentUser.isAdmin.toString().toBoolean()

        if (mUserProfileImageURL.isNotEmpty()){
            userHashMap["userProfilePic"] = mUserProfileImageURL
        }

        updateUserProfileData(this, userHashMap)
    }


    private fun setupActionBar(){

        setSupportActionBar(binding.toolbarSettingsActivity)

        val actionBar = supportActionBar

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white_24)
        }
        actionBar?.title = ""
        binding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }

        binding.btnSave.visibility = View.INVISIBLE
        binding.btnCancel.visibility = View.INVISIBLE
    }

    fun showImageChooser(activity: Activity){
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }


    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().uid.toString()).setValue(userHashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "User Data Updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error while updating the user details", Toast.LENGTH_SHORT).show()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details",
                    e
                )
            }
    }


    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "userProfilePic" + System.currentTimeMillis() + "."
                    + getFileExtension(
                activity,
                imageFileURI
            )
        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when (activity){
                        is ProfileViewActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }
        }
            .addOnFailureListener { exception ->
                when (activity){
                    is ProfileViewActivity -> {
                        Toast.makeText(
                            this, "Oops, Ocurred an error while tryong to upload the image!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message.toString(),
                    exception
                )
            }
    }
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser(this)
            } else {
                Toast.makeText(
                    this, "Oops, you just denied the permission for storage. You can also allow it from settings.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                if (data != null){
                    try {

                        mSelectedImageUri = data.data!!
                        GlideLoader(this).loadUserPicture(mSelectedImageUri!!, binding.ivUserPhoto)

                    } catch (e: IOException){

                        e.printStackTrace()
                        Toast.makeText(this,
                            "Image selection Failed!",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
        } else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    fun imageUploadSuccess(imageURL: String){
        //hideProgressDialog()
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}