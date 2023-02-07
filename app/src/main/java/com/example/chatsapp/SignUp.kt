package com.example.chatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var editUsername:EditText
    private lateinit var editEmail:EditText
    private lateinit var editPassword:EditText
    private lateinit var signUpBtn: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initialize()

        signUpBtn.setOnClickListener {
            val username = editUsername.text.toString()
            val email =  editEmail.text.toString()
            val password = editPassword.text.toString()
            signUp(username, email, password)
        }
    }
    private fun initialize() {
        supportActionBar?.hide()

        editUsername  =  findViewById(R.id.username_text)
        editEmail = findViewById(R.id.email_text)
        editPassword = findViewById(R.id.password_text)
        signUpBtn  =  findViewById(R.id.signup_btn)

        auth =  FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance("https://chatsapp-3480b-default-rtdb." +
                "europe-west1.firebasedatabase.app/").reference
    }


    private fun signUp(username:String, email:String, password:String) {
        //  Signup user logic
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // If task is successful, show a message and add user to the database
                    addUserToDatabase(username, email, auth.currentUser?.uid!!)
                    Toast.makeText(baseContext, "Successfully signed up.",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Failed to sign up",
                        Toast.LENGTH_SHORT).show()


                }
            }
    }

    private fun addUserToDatabase(username: String, email: String, uid: String){
        // Creating a user and adding it to the database

        dbRef.child("user").child(uid).setValue(User(username, email, uid))
    }

}