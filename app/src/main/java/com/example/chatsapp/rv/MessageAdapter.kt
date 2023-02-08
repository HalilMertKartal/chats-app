package com.example.chatsapp.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsapp.R
import com.example.chatsapp.data.Message
import com.example.chatsapp.data.MessageType
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // Define 2 inner classes (holders) because there are 2
    // different layouts to be inflated in this screen
    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageSent: TextView = itemView.findViewById(R.id.sent_message)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageReceived: TextView = itemView.findViewById(R.id.received_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var holder: RecyclerView.ViewHolder
        when {
            MessageType.SENT.num == viewType -> {
                // Inflate with sent messages
                val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
                holder = SentViewHolder(view)
            }
            MessageType.RECEIVED.num == viewType -> {
                // Inflate with received messages
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
                holder = ReceiveViewHolder(view)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // We need separate holders for sent messages and received messages
        val currentMessage = messageList[position].message
        if (holder.javaClass == SentViewHolder::class.java) {
            // SentViewHolder declared
            holder as SentViewHolder
            holder.messageSent.text = currentMessage
        } else {
            //  ReceiveViewHolder declared
            holder as ReceiveViewHolder
            holder.messageReceived.text = currentMessage
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].senderId.equals(FirebaseAuth.getInstance().currentUser?.uid)) {
            // User is the sender
            MessageType.SENT.num
        } else {
            // User is the receiver
            MessageType.RECEIVED.num
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}