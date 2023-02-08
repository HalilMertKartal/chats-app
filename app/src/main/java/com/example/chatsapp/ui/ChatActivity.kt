package com.example.chatsapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsapp.data.Message
import com.example.chatsapp.rv.MessageAdapter
import com.example.chatsapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: TextView
    private lateinit var sendMessageButton: ImageView

    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    private lateinit var senderUid: String
    private lateinit var username: String
    private lateinit var receiverUid: String

    private lateinit var receiverRoom: String
    private lateinit var senderRoom: String

    // Inflate the menu after it's created
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // When delete message selected, delete.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_msg) {
            // Delete the messages for only current user
            dbRef.child("chats").child(senderRoom).child("messages").
                removeValue()
            // Remove from render list
            messageList.clear()
            return true
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initialize()
        showMessages()

        sendMessageButton.setOnClickListener {
            // Add message to the database
            val messageContent = messageBox.text.toString()
            val message = Message(messageContent, senderUid)

            dbRef.child("chats").child(senderRoom).child("messages").push().setValue(message)
                .addOnSuccessListener {
                    dbRef.child("chats").child(receiverRoom).child("messages")
                        .push().setValue(message)
                }
                .addOnSuccessListener {
                    // If all went well, message sent  and database is updated.
                    // Now clear the message box
                    messageBox.text = ""

                }
        }
    }

    private fun initialize() {
        messageRecyclerView = findViewById(R.id.message_recycler_view)
        // also link the adapter with it
        messageList = arrayListOf()
        messageAdapter = MessageAdapter(this, messageList)
        messageRecyclerView.adapter = messageAdapter
        // finally add  the layout manager
        messageRecyclerView.layoutManager = LinearLayoutManager(this)

        messageBox = findViewById(R.id.edit_message)
        sendMessageButton = findViewById(R.id.send_icon)

        username = intent.getStringExtra("username")!!
        receiverUid = intent.getStringExtra("uid")!!

        supportActionBar?.title = username

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance(
            "https://chatsapp-3480b-default-rtdb." +
                    "europe-west1.firebasedatabase.app/"
        ).reference

        senderUid = auth.currentUser?.uid!!
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
    }

    // Show the messages by getting them from database add adding them to the recycler view
    private fun showMessages() {
        // Scroll down initially
        messageRecyclerView.scrollToPosition(messageList.size - 1)
        dbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    snapshot.children.forEach {
                        val currentMessage = it.getValue(Message::class.java)
                        messageList.add(currentMessage!!)

                    }
                    // Notify the recycler adapter
                    messageAdapter.notifyDataSetChanged()
                    // Scroll down the screen
                    messageRecyclerView.scrollToPosition(messageList.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}