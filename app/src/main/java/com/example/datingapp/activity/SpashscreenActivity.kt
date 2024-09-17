package com.example.datingapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.auth.loginActivity
import com.google.firebase.auth.FirebaseAuth

class SpashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spashscreen2)

        val user = FirebaseAuth.getInstance().currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (user == null) {
                startActivity(Intent(this, Getstarted::class.java))
            }
            else{
                startActivity(Intent(this, MainActivity::class.java))
            }
        },2000)




    }
}