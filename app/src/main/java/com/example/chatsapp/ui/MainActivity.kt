package com.example.chatsapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsapp.R
import com.example.chatsapp.data.User
import com.example.chatsapp.rv.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userlist: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        readUsers()
    }
    private fun initialize() {
        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)

        userlist = arrayListOf()
        userAdapter =  UserAdapter(this, userlist)
        userRecyclerView.adapter = userAdapter
        auth = FirebaseAuth.getInstance()
        dbRef =  FirebaseDatabase.getInstance("https://chatsapp-3480b-default-rtdb." +
                "europe-west1.firebasedatabase.app/").reference
    }

    // Inflate the menu after it's created
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    // When logout item is selected, log out.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId  == R.id.logout) {
            // Log out and go to login screen
            auth.signOut()
            val intent = Intent(this@MainActivity, Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }

    private fun readUsers() {
        // Get the user section of the database
        dbRef.child("user").addValueEventListener(object: ValueEventListener {
            // If data is changed

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                snapshot.children.forEach{
                    // Add all users to the userlist except current user
                    val currentUser = it.getValue(User::class.java)!!
                    if (currentUser.uid != auth.currentUser?.uid) {
                        userlist.add(currentUser)
                    }
                }
                // Notify recycle view adapter about data because it's changed
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}