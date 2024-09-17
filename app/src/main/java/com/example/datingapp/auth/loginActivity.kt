
package com.example.datingapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.R.layout.loadinglayout
import com.example.datingapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class loginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Assuming this function exists and sets edge-to-edge mode

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this).setView(loadinglayout)
            .setCancelable(false)
            .create()

        auth = FirebaseAuth.getInstance()

        binding.sendOtp.setOnClickListener {
            dialog.show()
            val phoneNumber = binding.usernumber.text.toString()
            if (phoneNumber.isEmpty()) {
                binding.usernumber.error = "Please Enter Your Number"
            } else {
                sendOtp(phoneNumber)
            }

        }

        binding.verifyotp.setOnClickListener {
            dialog.show()

            val otp = binding.userotp.text.toString()
            if (otp.isEmpty()) {
                binding.userotp.error = "Please Enter OTP"
            } else {
                verifyOtp(otp)
            }
        }
    }

    private fun sendOtp(number: String) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)

            }

            override fun onVerificationFailed(e: FirebaseException) {
                // Handle verification failure, show error message if needed
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@loginActivity.verificationId = verificationId
                dialog.dismiss()

                binding.sendOtp.visibility = android.view.View.GONE;
                binding.verifyOtp.visibility =android.view.View.VISIBLE;
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        signInWithPhoneAuthCredential(credential)


    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


                    checkUserExist(binding.usernumber.text.toString())


                } else {
                    dialog.dismiss()
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserExist(number: String) {

        FirebaseDatabase.getInstance().getReference("users").child("+91$number")
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    dialog.dismiss()

                    Toast.makeText(this@loginActivity, p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        dialog.dismiss()

                        startActivity(Intent(this@loginActivity, MainActivity::class.java))
                         finish()
                    }
                    else{
                        startActivity(Intent(this@loginActivity,RegisterActivity::class.java))
                        finish()
                    }
                }


            })

    }


}