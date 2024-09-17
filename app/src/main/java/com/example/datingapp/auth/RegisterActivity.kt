package com.example.datingapp.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.MainActivity
import com.example.datingapp.databinding.ActivityRegisterBinding
import com.example.datingapp.model.UserModel
import com.example.datingapp.util.Config
import com.example.datingapp.util.Config.hideDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private var imgUri: Uri? = null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imgUri = it

        binding.UserImage.setImageURI(imgUri)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.UserImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.registerBtn.setOnClickListener {
            validateData()
        }


    }

    private fun validateData() {
        val name = binding.UserName.text.toString()
        val email = binding.UserEmail.text.toString()
        val city = binding.UserCity.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter Your Name", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Please enter Your Email", Toast.LENGTH_SHORT).show()
        } else if (city.isEmpty()) {
            Toast.makeText(this, "Please enter Your City", Toast.LENGTH_SHORT).show()
        } else if (imgUri == null) {
            Toast.makeText(this, "Please Upload your Image", Toast.LENGTH_SHORT).show()
        } else if (!binding.termsandcondtions.isChecked) {
            Toast.makeText(this, "Please Accept all Terms and Condtions", Toast.LENGTH_SHORT).show()
        } else {
            uploadImg()

        }
    }

    private fun uploadImg()  {
        Config.showdialog(this)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            hideDialog()
            Toast.makeText(this, "User is not authenticated", Toast.LENGTH_SHORT).show()
            return
        }



            if (imgUri == null) {
                hideDialog()
                Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show()
                return
            }

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(currentUser.uid)
            .child("profile.jpg")

        storageRef.putFile(imgUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    storeData(uri)
                }.addOnFailureListener { e ->
                    hideDialog()
                    Toast.makeText(this, "IMG 1: Something went wrong: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                hideDialog()
                Toast.makeText(this, "IMG 2: Something went wrong: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun storeData(imageUrl: Uri?) {

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            hideDialog()
            Toast.makeText(this, "User is not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val phoneNumber = currentUser.phoneNumber ?: ""
        val data = UserModel(
            number = phoneNumber,
            name = binding.UserName.text.toString(),
            email = binding.UserEmail.text.toString(),
            city = binding.UserCity.text.toString(),
            image = imageUrl.toString()
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data).addOnCompleteListener{ task ->
                hideDialog()
                if (task.isSuccessful){
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Failed to register: ${task.exception?.message}", Toast.LENGTH_SHORT).show()

                }
            }


    }
}