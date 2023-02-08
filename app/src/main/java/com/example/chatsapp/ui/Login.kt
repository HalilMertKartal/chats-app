package com.example.chatsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chatsapp.R
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button
    private lateinit var auth: FirebaseAuth

    private fun initialize() {
        supportActionBar?.hide()

        editEmail = findViewById(R.id.email_text)
        editPassword =  findViewById(R.id.password_text)
        loginBtn =  findViewById(R.id.login_btn)
        signupBtn = findViewById(R.id.signup_btn)

        auth =  FirebaseAuth.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialize()

        signupBtn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            login(email, password)
        }

    }

    private fun login(email: String, password:String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login in success, update UI with the signed-in user's information
                    val intent =  Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                    Toast.makeText(baseContext, "Successfully logged in.",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // If login fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
}